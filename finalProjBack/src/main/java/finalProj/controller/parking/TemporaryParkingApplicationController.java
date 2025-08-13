package finalProj.controller.parking;

import java.util.Date;
import java.util.List;
import java.util.Optional;
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

import finalProj.domain.parking.ParkingSlot;
import finalProj.domain.parking.ParkingType;
import finalProj.domain.parking.TemporaryParkingApplication;
import finalProj.domain.users.Units;
import finalProj.dto.parking.ApiResponse;
import finalProj.dto.parking.TemporaryParkingDTO;
import finalProj.repository.parking.ParkingSlotRepository;
import finalProj.repository.parking.ParkingTypeRepository;
import finalProj.repository.parking.TemporaryParkingApplicationRepository;
import finalProj.repository.users.UnitsRepository;
import finalProj.service.parking.TemporaryParkingApplicationService;

@RestController
@RequestMapping("/park/temporary-parking")
public class TemporaryParkingApplicationController {

	@Autowired
	private TemporaryParkingApplicationService service;

	@Autowired
	private TemporaryParkingApplicationRepository repository;

	@Autowired
	private ParkingSlotRepository parkingSlotRepository;

	@Autowired
	private ParkingTypeRepository parkingTypeRepository;

	@Autowired
	private UnitsRepository unitsRepository;

	// 查詢可臨時停車車位
	@GetMapping("/available-slots")
	public ResponseEntity<?> getAvailableSlots(@RequestParam Integer communityId, @RequestParam Integer typeId,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end) {
		List<ParkingSlot> result = parkingSlotRepository.findAvailableTempSlots(communityId, typeId, start, end);
		if (result.isEmpty()) {
			return ResponseEntity.ok(ApiResponse.failure("查無資料"));
		}
		return ResponseEntity.ok(ApiResponse.success("查詢成功", result));
	}

	// 查詢某社區所有臨停申請紀錄（透過 ParkingSlot → Community）
	@GetMapping
	public ResponseEntity<ApiResponse<List<TemporaryParkingDTO>>> getAll(
			@RequestParam("communityId") Integer communityId) {
		List<TemporaryParkingApplication> records = repository.findByParkingSlot_Community_CommunityId(communityId);

		if (records == null || records.isEmpty()) {
			return ResponseEntity.ok(ApiResponse.success("無臨停申請紀錄", List.of()));
		}

		List<TemporaryParkingDTO> dtoList = records.stream().map(app -> {
			TemporaryParkingDTO dto = new TemporaryParkingDTO();
			dto.setId(app.getId());
			dto.setVisitorName(app.getVisitorName());
			dto.setLicensePlate(app.getLicensePlate());
			dto.setPurpose(app.getPurpose());
			dto.setRequestTime(app.getRequestTime());
			dto.setStartTime(app.getStartTime());
			dto.setEndTime(app.getEndTime());

			if (app.getParkingSlot() != null) {
				dto.setSlotNumber(app.getParkingSlot().getSlotNumber());
				dto.setLocation(app.getParkingSlot().getLocation());
			}

			if (app.getParkingType() != null) {
				dto.setParkingTypeId(app.getParkingType().getId());
			}

			if (app.getUnits() != null) {
				dto.setUnitsId(app.getUnits().getUnitsId());
			}

			return dto;
		}).collect(Collectors.toList());

		return ResponseEntity.ok(ApiResponse.success("查詢成功", dtoList));
	}

	// 新增臨停申請紀錄
	@PostMapping
	public ResponseEntity<ApiResponse<TemporaryParkingApplication>> create(@RequestBody TemporaryParkingDTO dto,
			@RequestParam("communityId") Integer communityId) {
		try {
			TemporaryParkingApplication app = new TemporaryParkingApplication();
			app.setVisitorName(dto.getVisitorName());
			app.setLicensePlate(dto.getLicensePlate());
			app.setPurpose(dto.getPurpose());
			app.setRequestTime(new Date());
			app.setStartTime(dto.getStartTime());
			app.setEndTime(dto.getEndTime());

			ParkingSlot slot = parkingSlotRepository.findBySlotNumberAndCommunity_CommunityId(dto.getSlotNumber(),
					communityId);
			if (slot == null) {
				return ResponseEntity.badRequest().body(ApiResponse.failure("找不到車位"));
			}
			app.setParkingSlot(slot);

			Optional<ParkingType> type = parkingTypeRepository.findById(dto.getParkingTypeId());

			if (type.isEmpty()) {
				return ResponseEntity.badRequest().body(ApiResponse.failure("找不到車位種類"));
			}
			app.setParkingType(type.get());

			Units units = unitsRepository.findByUnitsId(dto.getUnitsId());
			if (units == null) {
				return ResponseEntity.badRequest().body(ApiResponse.failure("找不到對應戶號"));
			}
			app.setUnits(units);

			TemporaryParkingApplication saved = service.create(app);
			if (saved == null) {
				return ResponseEntity.badRequest().body(ApiResponse.failure("新增失敗：Service驗證失敗"));
			}
			return ResponseEntity.ok(ApiResponse.success("新增成功", saved));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("新增失敗：" + e.getMessage()));
		}
	}

	// 修改臨停申請紀錄
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<TemporaryParkingApplication>> update(@RequestBody TemporaryParkingDTO dto,
			@PathVariable Integer id, @RequestParam("communityId") Integer communityId) {
		try {
			dto.setId(id);

			TemporaryParkingApplication app = new TemporaryParkingApplication();
			app.setId(dto.getId());
			app.setVisitorName(dto.getVisitorName());
			app.setLicensePlate(dto.getLicensePlate());
			app.setPurpose(dto.getPurpose());
			app.setRequestTime(dto.getRequestTime());
			app.setStartTime(dto.getStartTime());
			app.setEndTime(dto.getEndTime());

			ParkingSlot slot = parkingSlotRepository.findBySlotNumberAndCommunity_CommunityId(dto.getSlotNumber(),
					communityId);
			if (slot == null) {
				return ResponseEntity.badRequest().body(ApiResponse.failure("找不到車位"));
			}
			app.setParkingSlot(slot);

			ParkingType type = parkingTypeRepository.findById(dto.getParkingTypeId()).get();
			if (type == null) {
				return ResponseEntity.badRequest().body(ApiResponse.failure("找不到車位種類"));
			}
			app.setParkingType(type);

			Units units = unitsRepository.findByUnitsId(dto.getUnitsId());
			if (units == null) {
				return ResponseEntity.badRequest().body(ApiResponse.failure("找不到戶號"));
			}
			app.setUnits(units);

			TemporaryParkingApplication updated = service.update(app);
			return ResponseEntity.ok(ApiResponse.success("更新成功", updated));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("更新失敗：" + e.getMessage()));
		}
	}

	// 刪除臨停申請紀錄
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Boolean>> delete(@PathVariable Integer id) {
		boolean success = service.delete(id);
		if (success) {
			return ResponseEntity.ok(ApiResponse.success("刪除成功", true));
		} else {
			return ResponseEntity.badRequest().body(ApiResponse.failure("刪除失敗或找不到紀錄"));
		}
	}

	// 驗證時段重疊
	@PostMapping("/overlap")
	public Boolean isOverlapping(@RequestBody TemporaryParkingApplication application) {
		return service.isOverlapping(application.getStartTime(), application.getEndTime(),
				application.getParkingSlot().getId(), application.getId());
	}
}
