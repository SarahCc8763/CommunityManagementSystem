package finalProj.repository.parking;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.parking.LotteryEventSpace;

public interface LotteryEventSpaceRepository extends JpaRepository<LotteryEventSpace, Integer> {

	void deleteByLotteryEventsId(Integer id);

	List<LotteryEventSpace> findByLotteryEventsId(Integer eventId);
}
