package finalProj.repository.parking;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.parking.LotteryEventSpace;

public interface LotteryEventSpaceRepository extends JpaRepository<LotteryEventSpace, Integer> {

	void deleteByLotteryEvents_BulletinId(Integer bulletinId);

	List<LotteryEventSpace> findByLotteryEvents_BulletinId(Integer bulletinId);

	boolean existsByLotteryEvents_BulletinId(Integer id);
}
