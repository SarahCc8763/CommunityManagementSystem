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

import finalProj.domain.ParkingType;
import finalProj.service.ParkingTypeService;

//車位種類 Controller
@RestController
@RequestMapping("/park/parking-types")
public class ParkingTypeController {

	@Autowired
	private ParkingTypeService service;

	// 查詢所有車位種類
	@GetMapping
	public List<ParkingType> getAll() {
		return service.findAll();
	}

	// 新增車位種類
	@PostMapping
	public ParkingType create(@RequestBody ParkingType type) {
		return service.create(type);
	}

	// 修改車位種類
	@PutMapping("/{id}")
	public ParkingType update(@RequestBody ParkingType type, @PathVariable Integer id) {
		type.setId(id);
		return service.update(type);
	}

	// 刪除車位種類
	@DeleteMapping("/{id}")
	public Boolean delete(@PathVariable Integer id) {
		return service.delete(id);
	}

}
