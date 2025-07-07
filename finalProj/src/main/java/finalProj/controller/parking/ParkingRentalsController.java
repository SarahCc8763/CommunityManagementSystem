package finalProj.controller.parking;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.community.Community;
import finalProj.domain.parking.ParkingRentals;
import finalProj.domain.parking.ParkingSlot;
import finalProj.domain.users.Users;
import finalProj.dto.parking.ApiResponse;
import finalProj.dto.parking.RentalHistoryDTO;
import finalProj.repository.community.CommunityRepository;
import finalProj.repository.parking.ParkingRentalsRepository;
import finalProj.repository.parking.ParkingSlotRepository;
import finalProj.repository.parking.ParkingTypeRepository;
import finalProj.repository.users.UsersRepository;
import finalProj.service.parking.ParkingRentalsService;

// 承租紀錄 Controller
@RestController
@RequestMapping("/park/parking-rentals")
public class ParkingRentalsController {

	@Autowired
	private ParkingRentalsService service;

	@Autowired
	private ParkingRentalsRepository repository;

	@Autowired
	private ParkingSlotRepository parkingSlotRepository;

	@Autowired
	private ParkingTypeRepository parkingTypeRepository;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private CommunityRepository communityRepository;

	// 查詢某社區所有承租紀錄
	@GetMapping
	public ResponseEntity<ApiResponse<List<RentalHistoryDTO>>> getAll(
			@RequestParam("communityId") Integer communityId) {
		List<ParkingRentals> records = repository.findByCommunity_CommunityId(communityId);

		if (records == null || records.isEmpty()) {
			return ResponseEntity.ok(ApiResponse.success("無承租紀錄", List.of())); // 回傳空 List，而不是 entity
		}

		List<RentalHistoryDTO> dtoList = records.stream().map(record -> {
			RentalHistoryDTO dto = new RentalHistoryDTO();
			dto.setId(record.getId());
			dto.setLicensePlate(record.getLicensePlate());
			dto.setRentBuyStart(record.getRentBuyStart());
			dto.setRentEnd(record.getRentEnd());
			dto.setStatus(record.getStatus());
			dto.setCreatedAt(record.getCreatedAt());
			dto.setUpdatedAt(record.getUpdatedAt());
			dto.setApproved(record.getApproved());

			// 關聯的部分
			ParkingSlot parkingSlot = record.getParkingSlot();
			if (parkingSlot != null) {
				dto.setSlotNumber(parkingSlot.getSlotNumber());
				dto.setLocation(parkingSlot.getLocation());
				dto.setParkingType(parkingSlot.getParkingType().getType());
			}

			if (record.getUsers() != null) {
				dto.setUserName(record.getUsers().getName());
				dto.setUsersId(record.getUsers().getUsersId());
			}

			if (record.getApprover() != null) {
				dto.setApproverName(record.getApprover().getName());
				dto.setApproverId(record.getApprover().getUsersId());
			}
			return dto;
		}).collect(Collectors.toList());

		return ResponseEntity.ok(ApiResponse.success("查詢成功", dtoList));
	}

	// 新增承租紀錄
	@PostMapping
	public ResponseEntity<ApiResponse<ParkingRentals>> create(@RequestBody RentalHistoryDTO rentalDTO,
			@RequestParam("communityId") Integer communityId) {
		try {
			// ✅ 初始化 Entity 並轉入 DTO 資料
			ParkingRentals rental = new ParkingRentals();
			rental.setLicensePlate(rentalDTO.getLicensePlate());
			rental.setRentBuyStart(rentalDTO.getRentBuyStart());
			rental.setRentEnd(rentalDTO.getRentEnd());
			rental.setStatus(rentalDTO.getStatus());
			rental.setApproved(rentalDTO.getApproved() != null ? rentalDTO.getApproved() : false);
			rental.setCreatedAt(new Date());
			rental.setUpdatedAt(new Date());

			// 🔗 查找並設定社區
			Community community = communityRepository.findByCommunityId(communityId);
			if (community == null) {
				return ResponseEntity.badRequest().body(ApiResponse.failure("找不到社區"));
			}
			rental.setCommunity(community);

			// 🔗 查找並設定車位
			ParkingSlot slot = parkingSlotRepository.findBySlotNumberAndCommunity_CommunityId(rentalDTO.getSlotNumber(),
					communityId);
			if (slot == null) {
				return ResponseEntity.badRequest().body(ApiResponse.failure("找不到對應的車位"));
			}
			rental.setParkingSlot(slot);

			// 🔗 查找並設定承租人
			Users user = usersRepository.findByUsersId(rentalDTO.getUsersId());
			if (user == null) {
				return ResponseEntity.badRequest().body(ApiResponse.failure("找不到承租者"));
			}
			rental.setUsers(user);

			// 🔗 若有審核人
			if (rentalDTO.getApproverName() != null) {
				Users approver = usersRepository.findByName(rentalDTO.getApproverName());
				if (approver != null) {
					rental.setApprover(approver);
				}
			}

			// ✅ 儲存資料
			ParkingRentals saved = service.create(rental);
			if (saved == null) {
				return ResponseEntity.badRequest().body(ApiResponse.failure("新增失敗：輸入格式錯誤或時段重疊"));
			}
			return ResponseEntity.ok(ApiResponse.success("新增成功", saved));

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("新增失敗：" + e.getMessage()));
		}
	}

	// 修改承租紀錄
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<ParkingRentals>> update(@RequestBody RentalHistoryDTO rentalDTO,
			@PathVariable Integer id,
			@RequestParam("communityId") Integer communityId) {
		try {
			rentalDTO.setId(id); // 確保 ID 同步

			// 🔄 將 DTO 轉換為 Entity
			ParkingRentals rental = new ParkingRentals();
			rental.setId(rentalDTO.getId());
			rental.setLicensePlate(rentalDTO.getLicensePlate());
			rental.setRentBuyStart(rentalDTO.getRentBuyStart());
			rental.setRentEnd(rentalDTO.getRentEnd());
			rental.setStatus(rentalDTO.getStatus());
			rental.setApproved(rentalDTO.getApproved());
			rental.setCreatedAt(rentalDTO.getCreatedAt());
			rental.setUpdatedAt(new Date()); // 更新時間

			Community community = communityRepository.findByCommunityId(communityId);

			if (community == null) {
				return ResponseEntity.badRequest().body(ApiResponse.failure("找不到社區"));
			}
			rental.setCommunity(community);

			// 🔗 關聯 ParkingSlot（由 slotNumber 找）
			ParkingSlot slot = parkingSlotRepository.findBySlotNumberAndCommunity_CommunityId(rentalDTO.getSlotNumber(),
					communityId);

			if (slot == null) {
				return ResponseEntity.badRequest().body(ApiResponse.failure("找不到對應的車位"));
			}
			rental.setParkingSlot(slot);

			// 🔗 關聯 Users（由 userName 找）
			Users user = usersRepository.findByUsersId(rentalDTO.getUsersId());
			if (user == null) {
				return ResponseEntity.badRequest().body(ApiResponse.failure("找不到承租者"));
			}
			rental.setUsers(user);

			// 🔗 若有審核人（approverName）
			if (rentalDTO.getApproverName() != null) {
				Users approver = usersRepository.findByName(rentalDTO.getApproverName());
				if (approver != null) {
					rental.setApprover(approver);
				}
			}

			ParkingRentals updated = service.update(rental);
			if (updated == null) {
				return ResponseEntity.badRequest().body(ApiResponse.failure("修改失敗：輸入格式錯誤或時段重疊"));
			}
			return ResponseEntity.ok(ApiResponse.success("更新成功", updated));

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("更新失敗：" + e.getMessage()));
		}
	}

	// 刪除承租紀錄
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Boolean>> delete(@PathVariable Integer id) {
		boolean success = service.delete(id);
		if (success) {
			return ResponseEntity.ok(ApiResponse.success("刪除成功", true));
		} else {
			return ResponseEntity.badRequest().body(ApiResponse.failure("刪除失敗或找不到紀錄"));
		}
	}

	// 查詢可承租車位
	@GetMapping("/available-slots")
	public ResponseEntity<ApiResponse<List<ParkingSlot>>> findAvailableSlots(@RequestParam Integer parkingTypeId,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) {

		if (parkingTypeRepository.findById(parkingTypeId) == null) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("找不到車位種類"));
		}

		List<ParkingSlot> availableSlots = service.findAvailableSlots(parkingTypeId, start, end);

		return ResponseEntity.ok(ApiResponse.success(availableSlots));
	}

	// 查詢某車位的承租歷史
	// @GetMapping("/{slotId}/history")
	// public List<RentalHistoryDTO> getRentalHistory(
	// @PathVariable Integer slotId,
	// @RequestParam String range
	// ) {
	// // 1. 找出該 slotNumber 對應的 slot id
	// Optional<ParkingSlot> slotOpt = parkingSlotRepository.findById(slotId);
	// if (slotOpt.isEmpty()) {
	// throw new ResponseStatusException(HttpStatus.NOT_FOUND, "車位不存在");
	// }
	//
	//
	// // 2. 轉換 range 為 Date 範圍
	// Date startDate = switch (range) {
	// case "1" -> getDateYearsAgo(1);
	// case "3" -> getDateYearsAgo(3);
	// case "5" -> getDateYearsAgo(5);
	// case "all" -> null;
	// default -> throw new IllegalArgumentException("無效的範圍參數");
	// };
	//
	// // 3. 查詢資料
	// List<ParkingRentals> rentals =
	// parkingRentalsRepository.findHistoryBySlotIdAndStartDate(1,slotId,
	// startDate);
	//
	// // 4. 回傳前端格式
	// return rentals.stream()
	// .map(r -> new RentalHistoryDTO(
	// r.getLicensePlate(),
	// r.getRentBuyStart(),
	// r.getRentEnd(),
	// r.getStatus()
	// ))
	// .collect(Collectors.toList());
	// }
	//
	// private Date getDateYearsAgo(int years) {
	// Calendar cal = Calendar.getInstance();
	// cal.add(Calendar.YEAR, -years);
	// return cal.getTime();
	// }

	// 驗證時段重疊
	@PostMapping("/overlap")
	public Boolean isOverlapping(@RequestBody ParkingRentals rental) {
		return service.isOverlapping(rental.getRentBuyStart(), rental.getRentEnd(), rental.getParkingSlot().getId(),
				rental.getId());
	}

}
