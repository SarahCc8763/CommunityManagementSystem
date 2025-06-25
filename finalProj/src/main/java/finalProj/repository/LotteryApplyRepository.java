package finalProj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.LotteryApply;

public interface LotteryApplyRepository extends JpaRepository<LotteryApply, Integer> {

	boolean existsByUsersIdAndLotteryEventsId(Integer usersId, Integer lotteryEventsId);
	
	List<LotteryApply> findByLotteryEventsId(Integer lotteryEventsId);
}
