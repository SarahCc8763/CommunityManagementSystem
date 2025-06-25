package finalProj.controller;

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

import finalProj.domain.ParkingRentals;
import finalProj.service.ParkingRentalsService;

// 承租紀錄 Controller
@RestController
@RequestMapping("/park/parking-rentals")
public class ParkingRentalsController {

	@Autowired
	private ParkingRentalsService service;

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
