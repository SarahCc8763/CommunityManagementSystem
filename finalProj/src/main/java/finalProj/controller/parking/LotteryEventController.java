package finalProj.controller.parking;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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

import finalProj.domain.parking.LotteryEvents;
import finalProj.dto.parking.ApiResponse;
import finalProj.dto.parking.DrawLotsResultDTO;
import finalProj.dto.parking.LotteryEventSpaceDTO;
import finalProj.dto.parking.LotteryEventUpdateRequest;
import finalProj.repository.parking.LotteryEventRepository;
import finalProj.service.parking.LotteryEventService;

// 抽籤活動 Controller
@RestController
@RequestMapping("/park/lottery-event")
public class LotteryEventController {

	@Autowired
	private LotteryEventService service;

	@Autowired
	private LotteryEventRepository repository;

	// 查詢某抽籤活動
	@GetMapping("{id}")
	public ResponseEntity<ApiResponse<LotteryEventUpdateRequest>> getEvent(
			@PathVariable("id") Integer id) {
		Optional<LotteryEvents> entities = repository.findById(id);

		if (entities == null || entities.isEmpty()) {
			return ResponseEntity.ok(ApiResponse.success("無抽籤活動", null));
		}
		LotteryEvents event = entities.get();
		LotteryEventUpdateRequest dto = new LotteryEventUpdateRequest();
		dto.setId(event.getBulletinId());
		dto.setTitle(event.getTitle());
		dto.setStartedAt(event.getStartedAt());
		dto.setEndedAt(event.getEndedAt());
		dto.setRentalStart(event.getRentalStart());
		dto.setRentalEnd(event.getRentalEnd());
		dto.setUsersId(event.getUsers().getUsersId());
		dto.setCreatedAt(event.getCreatedAt());
		dto.setTypeId(event.getParkingType().getId());
		dto.setStatus(event.getStatus());

		Set<LotteryEventSpaceDTO> slotDtos = event.getLotteryEventSpace().stream().map(slot -> {
			LotteryEventSpaceDTO sDto = new LotteryEventSpaceDTO();
			sDto.setId(slot.getId());
			sDto.setParkingSlotId(slot.getParkingSlot().getId());
			return sDto;
		}).collect(Collectors.toSet());
		dto.setParkingSlotIds(slotDtos);

		return ResponseEntity.ok(ApiResponse.success("查詢成功", dto));
	}

	// 查詢某社區所有抽籤活動
	@GetMapping
	public ResponseEntity<ApiResponse<List<LotteryEventUpdateRequest>>> getAll(
			@RequestParam("communityId") Integer communityId) {
		List<LotteryEvents> entities = repository.findByCommunityId(communityId);

		if (entities == null || entities.isEmpty()) {
			return ResponseEntity.ok(ApiResponse.success("無抽籤活動", List.of())); // 回傳空 List，而不是 entity
		}

		List<LotteryEventUpdateRequest> dtos = entities.stream().map(event -> {
			LotteryEventUpdateRequest dto = new LotteryEventUpdateRequest();
			dto.setId(event.getBulletinId());
			dto.setTitle(event.getTitle());
			dto.setStartedAt(event.getStartedAt());
			dto.setEndedAt(event.getEndedAt());
			dto.setRentalStart(event.getRentalStart());
			dto.setRentalEnd(event.getRentalEnd());
			dto.setUsersId(event.getUsers().getUsersId());
			dto.setCreatedAt(event.getCreatedAt());
			dto.setTypeId(event.getParkingType().getId());
			dto.setTypeName(event.getParkingType().getType());
			dto.setStatus(event.getStatus());

			Set<LotteryEventSpaceDTO> slotDtos = event.getLotteryEventSpace().stream().map(slot -> {
				LotteryEventSpaceDTO sDto = new LotteryEventSpaceDTO();
				sDto.setId(slot.getId());
				sDto.setParkingSlotId(slot.getParkingSlot().getId());
				return sDto;
			}).collect(Collectors.toSet());
			dto.setParkingSlotIds(slotDtos);

			return dto;
		}).collect(Collectors.toList());

		return ResponseEntity.ok(ApiResponse.success("查詢成功", dtos));
	}

	// 新增抽籤活動
	@PostMapping
	public ResponseEntity<ApiResponse<LotteryEventUpdateRequest>> create(
			@RequestParam("communityId") Integer communityId, @RequestBody LotteryEventUpdateRequest dto) {
		try {
			LotteryEventUpdateRequest saved = service.create(dto, communityId);
			if (saved == null) {
				System.out.println("有進到Controller");
				return ResponseEntity.badRequest().body(ApiResponse.failure("新增失敗：輸入資料錯誤"));
			}
			return ResponseEntity.ok(ApiResponse.success("新增成功", saved));
		} catch (Exception e) {
			System.out.println("有進到Controller");
			return ResponseEntity.badRequest().body(ApiResponse.failure("新增失敗：" + e.getMessage()));
		}
	}

	// 修改抽籤活動
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<LotteryEventUpdateRequest>> update(@PathVariable Integer id,
			@RequestBody LotteryEventUpdateRequest dto) {
		try {
			dto.setId(id);
			LotteryEventUpdateRequest updated = service.update(dto);
			if (updated == null) {
				System.out.println("有進到Controller");
				return ResponseEntity.badRequest().body(ApiResponse.failure("更新失敗：輸入資料錯誤"));
			}
			return ResponseEntity.ok(ApiResponse.success("更新成功", updated));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("更新失敗：" + e.getMessage()));
		}
	}

	// 刪除抽籤活動
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Boolean>> delete(@PathVariable Integer id) {
		boolean success = service.delete(id);
		System.out.println("刪除結果 = " + success);
		if (success) {
			return ResponseEntity.ok(ApiResponse.success("刪除成功", true));
		} else {
			return ResponseEntity.badRequest().body(ApiResponse.failure("刪除失敗或找不到資料"));
		}
	}

	// 抽籤
	@PutMapping("/draw/{eventId}")
	public ResponseEntity<ApiResponse<DrawLotsResultDTO>> drawLots(@PathVariable Integer eventId) {
		try {
			DrawLotsResultDTO result = service.drawLotsForEvent(eventId);
			return ResponseEntity.ok(ApiResponse.success("抽籤成功", result));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("抽籤失敗：" + e.getMessage()));
		}
	}
}
