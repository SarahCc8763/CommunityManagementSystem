package finalProj.controller.parking;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.parking.ParkingRentals;
import finalProj.domain.parking.ParkingSlot;
import finalProj.service.parking.ParkingRentalsService;

// 承租紀錄 Controller
@RestController
@RequestMapping("/park/parking-rentals")
public class ParkingRentalsController {

	@Autowired
	private ParkingRentalsService service;

	// 查詢可承租車位
	@GetMapping("/available-slots")
	public List<ParkingSlot> findAvailableSlots(
	    @RequestParam Integer parkingTypeId,
	    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
	    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end) {
	    
	    return service.findAvailableSlots(parkingTypeId, start, end);
	}

	
	// 查詢所有承租紀錄
	@GetMapping
	public List<ParkingRentals> getAll() {
		return service.findAll();
	}
	
	// 驗證時段重疊
	@PostMapping("/overlap")
	public Boolean isOverlapping(@RequestBody ParkingRentals rental) {
		return service.isOverlapping(rental.getRentBuyStart(),rental.getRentEnd(),rental.getParkingSlotId(), rental.getId());
	}	

	// 新增承租紀錄
	@PostMapping
	public ParkingRentals create(@RequestBody ParkingRentals rental) {
		return service.create(rental);
	}

	// 修改承租紀錄
	@PutMapping("/{id}")
	public ParkingRentals update(@RequestBody ParkingRentals rental, @PathVariable Integer id) {
		rental.setId(id);
		return service.update(rental);
	}

	// 刪除承租紀錄
	@DeleteMapping("/{id}")
	public Boolean delete(@PathVariable Integer id) {
		return service.delete(id);
	}

}
