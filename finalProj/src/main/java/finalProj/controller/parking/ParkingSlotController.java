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
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.parking.ParkingSlot;
import finalProj.dto.parking.ParkingSlotQueryDTO;
import finalProj.service.parking.ParkingSlotService;

//車位資料 Controller
@RestController
@RequestMapping("/park/parking-slots")
public class ParkingSlotController {

	@Autowired
	private ParkingSlotService service;

	// 查詢所有車位種類
	@GetMapping
	public List<ParkingSlot> getAll() {
		return service.findAll();
	}

	// 新增車位種類
	@PostMapping
	public ParkingSlot create(@RequestBody ParkingSlot slot) {
		return service.create(slot);
	}

	// 修改車位種類
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


	// 刪除車位種類
	@DeleteMapping("/{id}")
	public Boolean delete(@PathVariable Integer id) {
		return service.delete(id);
	}

}
