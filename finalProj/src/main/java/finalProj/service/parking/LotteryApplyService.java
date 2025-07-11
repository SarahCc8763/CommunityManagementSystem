package finalProj.service.parking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.parking.LotteryApply;
import finalProj.dto.parking.LotteryApplyDTO;
import finalProj.repository.parking.LotteryApplyRepository;
import jakarta.transaction.Transactional;

// 抽籤申請表 Service
@Service
@Transactional
public class LotteryApplyService {

	@Autowired
	private LotteryApplyRepository repository;
	
    public List<LotteryApply> findByUserId(Integer userId) {
        return repository.findByUsers_UsersId(userId);
    }

	public List<LotteryApplyDTO> findByEvent(Integer eventId) {
		List<LotteryApply> list = repository.findByLotteryEvents_BulletinId(eventId);

		return list.stream().map(apply -> {
			LotteryApplyDTO dto = new LotteryApplyDTO();
			dto.setId(apply.getId());
			dto.setUsersId(apply.getUsers().getUsersId());
			dto.setUserName(apply.getUsers().getName());
			dto.setEmail(apply.getUsers().getEmail());
			dto.setLotteryEventId(apply.getLotteryEvents().getBulletinId());
			dto.setEventTitle(apply.getLotteryEvents().getTitle());
			dto.setAppliedAt(apply.getAppliedAt());

			dto.setAwardedSlot(
					apply.getLotteryEventSpace() != null ? apply.getLotteryEventSpace().getParkingSlot().getSlotNumber()
							: null);

			return dto;
		}).toList();
	}

	public boolean hasAlreadyApplied(Integer eventId, Integer userId) {
		return repository.existsByLotteryEvents_BulletinIdAndUsers_UsersId(eventId, userId);
	}

	// 新增申請
	public LotteryApply apply(LotteryApply apply) {
		return repository.save(apply);
	}
}
