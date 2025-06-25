package finalProj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.LotteryEventSpace;

public interface LotteryEventSpaceRepository extends JpaRepository<LotteryEventSpace, Integer> {

	void deleteByLotteryEventsId(Integer id);

	List<LotteryEventSpace> findByLotteryEventsId(Integer eventId);
}
