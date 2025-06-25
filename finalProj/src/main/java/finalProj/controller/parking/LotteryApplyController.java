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

import finalProj.domain.parking.LotteryApply;
import finalProj.service.parking.LotteryApplyService;

// 抽籤申請表 Controller
@RestController
@RequestMapping("/park/lottery-apply")
public class LotteryApplyController {

	@Autowired
	private LotteryApplyService service;

	// 查詢所有抽籤申請表
	@GetMapping
	public List<LotteryApply> getAll() {
		return service.findAll();
	}

	// 新增抽籤申請表
	@PostMapping
	public LotteryApply create(@RequestBody LotteryApply apply) {
		return service.create(apply);
	}

	// 修改抽籤申請表
	@PutMapping("/{id}")
	public LotteryApply update(@RequestBody LotteryApply apply, @PathVariable Integer id) {
		apply.setId(id);
		return service.update(apply);
	}

	// 刪除抽籤申請表
	@DeleteMapping("/{id}")
	public Boolean delete(@PathVariable Integer id) {
		return service.delete(id);
	}

}
