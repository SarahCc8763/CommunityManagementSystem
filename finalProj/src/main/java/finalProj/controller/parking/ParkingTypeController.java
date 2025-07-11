package finalProj.controller.parking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.parking.ParkingType;
import finalProj.dto.parking.ApiResponse;
import finalProj.repository.parking.ParkingTypeRepository;
import finalProj.service.parking.ParkingTypeService;

// 車位種類 Controller
@RestController
@RequestMapping("/park/parking-types")
public class ParkingTypeController {

	@Autowired
	private ParkingTypeService service;

	@Autowired
	private ParkingTypeRepository repository;

	// 查詢某社區所有車位種類
	@GetMapping
	public ResponseEntity<ApiResponse<List<ParkingType>>> getAll(@RequestParam("communityId") Integer communityId) {
		List<ParkingType> type = repository.findByCommunity_CommunityId(communityId);
		if (type == null || type.isEmpty()) {
			return ResponseEntity.ok(ApiResponse.success("無車位種類資料", type));
		}
		return ResponseEntity.ok(ApiResponse.success("查詢成功", type));
	}

	// 新增或修改某社區的車位種類
	@PostMapping
	public ResponseEntity<ApiResponse<List<ParkingType>>> createOrUpdateBatch(@RequestBody List<ParkingType> inputTypes,
			@RequestParam("communityId") Integer communityId) {

		List<ParkingType> updatedList = service.replaceTypesByCommunity(communityId, inputTypes);

		if (updatedList == null) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("更新失敗，請檢查資料"));
		}

		return ResponseEntity.ok(ApiResponse.success("更新成功", updatedList));
	}

}
