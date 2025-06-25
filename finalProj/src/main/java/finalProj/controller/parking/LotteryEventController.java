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

import finalProj.domain.parking.LotteryEvents;
import finalProj.dto.parking.DrawLotsResultDTO;
import finalProj.dto.parking.LotteryEventUpdateRequest;
import finalProj.service.parking.LotteryEventService;

// 抽籤活動 Controller
@RestController
@RequestMapping("/park/lottery-event")
public class LotteryEventController {

	@Autowired
	private LotteryEventService service;

	// 查詢所有抽籤活動
	@GetMapping
	public List<LotteryEvents> getAll() {
		return service.findAll();
	}

	// 新增抽籤活動
	@PostMapping
	public LotteryEventUpdateRequest create(@RequestBody LotteryEventUpdateRequest event) {
		return service.create(event);
	}

	// 修改抽籤活動
	@PutMapping("/{id}")
	public LotteryEventUpdateRequest update(@RequestBody LotteryEventUpdateRequest event, @PathVariable Integer id) {
		event.setId(id);
		return service.update(event);
	}

	// 抽籤
	@PostMapping("/draw/{eventId}")
	public DrawLotsResultDTO drawLots(@PathVariable Integer eventId) {
		DrawLotsResultDTO result = service.drawLotsForEvent(eventId);
		return result;
	}

	// 刪除抽籤活動
	@DeleteMapping("/{id}")
	public Boolean delete(@PathVariable Integer id) {
		return service.delete(id);
	}

}
