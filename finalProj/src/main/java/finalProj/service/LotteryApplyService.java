package finalProj.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.LotteryApply;
import finalProj.domain.LotteryEvents;
import finalProj.repository.LotteryApplyRepository;
import finalProj.repository.LotteryEventRepository;
import finalProj.repository.UsersRepository;
import jakarta.transaction.Transactional;

// 抽籤申請表 Service
@Service
@Transactional
public class LotteryApplyService {

	@Autowired
	private LotteryApplyRepository repository;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private LotteryEventRepository lotteryEventRepository;

	// 查詢所有申請表
	public List<LotteryApply> findAll() {
		return repository.findAll();
	}

	// 新增申請表
	public LotteryApply create(LotteryApply apply) {
		Integer usersId = apply.getUsersId();
		Integer lotteryEventsId = apply.getLotteryEventsId();

		if (usersId == null || lotteryEventsId == null) {
			return null;
		}

		if (!usersRepository.existsById(usersId) || !lotteryEventRepository.existsById(lotteryEventsId)) {
			return null;
		}

		if (repository.existsByUsersIdAndLotteryEventsId(usersId, lotteryEventsId)) {
			return null;
		}

		Date currentTime = new Date();

		LotteryEvents lotteryEvent = lotteryEventRepository.findById(lotteryEventsId).get();
		if (currentTime.before(lotteryEvent.getStartedAt()) || currentTime.after(lotteryEvent.getEndedAt())) {
			return null;
		}
		apply.setAppliedAt(currentTime);

		return repository.save(apply);
	}
	
	// 修改申請表
	public LotteryApply update(LotteryApply apply) {
		Integer usersId = apply.getUsersId();
		Integer lotteryEventsId = apply.getLotteryEventsId();
		apply.setAppliedAt(new Date());
		return repository.save(apply);
	}

	// 刪除申請表
	public Boolean delete(Integer id) {
		if (id != null && repository.existsById(id)) {
			repository.deleteById(id);
			return true;
		}
		return false;
	}
}
