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

import finalProj.domain.parking.TemporaryParkingApplication;
import finalProj.service.parking.TemporaryParkingApplicationService;

// 臨停申請紀錄 Controller
@RestController
@RequestMapping("/park/temporary-parking")
public class TemporaryParkingApplicationController {

	@Autowired
	private TemporaryParkingApplicationService service;

	// 查詢所有臨停申請紀錄
	@GetMapping
	public List<TemporaryParkingApplication> getAll() {
		return service.findAll();
	}

	// 驗證時段重疊
	@PostMapping("/overlap")
	public Boolean isOverlapping(@RequestBody TemporaryParkingApplication application) {
		return service.isOverlapping(application.getStartTime(), application.getEndTime(),
				application.getParkingSlotId(),application.getId());
	}

	// 新增臨停申請紀錄
	@PostMapping
	public TemporaryParkingApplication create(@RequestBody TemporaryParkingApplication application) {
		return service.create(application);
	}

	// 修改臨停申請紀錄
	@PutMapping("/{id}")
	public TemporaryParkingApplication update(@RequestBody TemporaryParkingApplication application,
			@PathVariable Integer id) {
		application.setId(id);
		return service.update(application);
	}

	// 刪除臨停申請紀錄
	@DeleteMapping("/{id}")
	public Boolean delete(@PathVariable Integer id) {
		return service.delete(id);
	}

}
