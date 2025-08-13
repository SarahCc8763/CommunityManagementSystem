package finalProj.controller.parking;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.parking.LotteryApply;
import finalProj.domain.parking.LotteryEvents;
import finalProj.domain.users.Users;
import finalProj.dto.parking.ApiResponse;
import finalProj.dto.parking.LotteryApplyDTO;
import finalProj.dto.parking.WinnerDTO;
import finalProj.repository.parking.LotteryApplyRepository;
import finalProj.repository.parking.LotteryEventRepository;
import finalProj.repository.users.UsersRepository;
import finalProj.service.parking.LotteryApplyService;

// 抽籤申請表 Controller
@RestController
@RequestMapping("/park/lottery-apply")
public class LotteryApplyController {

	@Autowired
	private LotteryApplyRepository repository;

	@Autowired
	private LotteryApplyService service;

	@Autowired
	private LotteryEventRepository lotteryEventRepository;

	@Autowired
	private UsersRepository usersRepository;

	// 查詢中獎名單
	@GetMapping("/winners")
	public ResponseEntity<ApiResponse<List<WinnerDTO>>> getWinners(@RequestParam Integer eventId) {
		List<WinnerDTO> winners = repository.findWinnersByEventId(eventId);

		return ResponseEntity.ok(ApiResponse.success("中獎名單查詢成功", winners));
	}

	// 查詢使用者申請過的所有活動
	@GetMapping("/user/{userId}")
	public ResponseEntity<ApiResponse<List<LotteryApply>>> getByUserId(@PathVariable Integer userId) {
		List<LotteryApply> records = service.findByUserId(userId);
		return ResponseEntity.ok(ApiResponse.success("查詢成功", records));
	}

	// 查詢某抽籤活動的所有申請者
	@GetMapping("/{id}/participants")
	public ResponseEntity<ApiResponse<List<LotteryApplyDTO>>> getByEvent(@PathVariable("id") Integer id) {
		List<LotteryApplyDTO> list = service.findByEvent(id);
		return ResponseEntity.ok(ApiResponse.success("查詢成功", list));
	}

	// 送出申請（簡化版，僅用 eventId + userId）
	@PostMapping
	public ResponseEntity<ApiResponse<String>> apply(@RequestParam Integer eventId, @RequestParam Integer userId) {

		if (service.hasAlreadyApplied(eventId, userId)) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("您已申請過此活動"));
		}

		Optional<LotteryEvents> eventOp = lotteryEventRepository.findById(eventId);
		Optional<Users> userOp = usersRepository.findById(userId);

		if (eventOp.isEmpty() || userOp.isEmpty()) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("活動或使用者不存在"));
		}

		if (eventOp.get().getEndedAt().before(new Date())) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("已超過申請期限"));
		}

		if (eventOp.get().getStartedAt().after(new Date())) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("活動未開始"));
		}

		LotteryApply apply = new LotteryApply();
		apply.setLotteryEvents(eventOp.get());
		apply.setUsers(userOp.get());
		apply.setAppliedAt(new Date());

		service.apply(apply);
		return ResponseEntity.ok(ApiResponse.success("申請成功"));
	}

	// 取消申請
	@DeleteMapping("/{applyId}")
	public ResponseEntity<ApiResponse<Boolean>> deleteApply(@PathVariable Integer applyId) {
		if (!repository.existsById(applyId)) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("未提供申請序號"));
		}

		LotteryApply apply = repository.findById(applyId).orElse(null);
		if (apply == null) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("無申請紀錄"));
		}

		LotteryEvents event = apply.getLotteryEvents();
		if (event == null || Boolean.TRUE.equals(event.getStatus())) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("該活動已抽籤，無法取消申請"));
		}

		repository.deleteById(applyId);
		return ResponseEntity.ok(ApiResponse.success("已移除申請", true));
	}

}
