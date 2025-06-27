package finalProj.controller.parking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import finalProj.dto.parking.ParkingSlotQueryDTO;
import finalProj.repository.parking.ParkingSlotRepository;
import finalProj.service.parking.ParkingSlotService;

//車位資料 Controller
@RestController
@RequestMapping("/park/parking-slots")
public class ParkingSlotController {

	@Autowired
	private ParkingSlotService service;
	
	@Autowired
	ParkingSlotRepository repository;

	// 查詢某社區所有車位資料
	@GetMapping
	public List<ParkingSlot> getAll(@RequestParam Integer communityId) {
		return repository.findByCommunityId(communityId);
	}
	
	// 新增車位資料 (多筆)
	@PostMapping("/create-all")
	public List<ParkingSlot> createAll(@RequestBody List<ParkingSlot> slot) {
		return service.createAll(slot);
	}

	// 新增車位資料 (單筆)
	@PostMapping("/create")
	public ParkingSlot create(@RequestBody ParkingSlot slot) {
		return service.create(slot);
	}

	// 修改車位資料
	@PutMapping("/{id}")
	public ParkingSlot update(@RequestBody ParkingSlot slot, @PathVariable Integer id) {
		slot.setId(id);
		return service.update(slot);
	}
	
	// 查詢可抽籤車位
	@PostMapping("/available")
	public List<ParkingSlot> getAvailableSlots(@RequestBody ParkingSlotQueryDTO dto) {
	    List<ParkingSlot> result = service.findAvailableSlots(dto);
	    return result;
	}


	// 刪除車位資料
	@DeleteMapping("/{id}")
	public Boolean delete(@PathVariable Integer id) {
		return service.delete(id);
	}

}
