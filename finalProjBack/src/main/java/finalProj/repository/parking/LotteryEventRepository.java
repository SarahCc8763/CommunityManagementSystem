package finalProj.repository.parking;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import finalProj.domain.parking.LotteryEvents;

public interface LotteryEventRepository extends JpaRepository<LotteryEvents, Integer> {

	@Query("SELECT e FROM LotteryEvents e WHERE e.bulletin.community.communityId = :communityId")
	List<LotteryEvents> findByCommunityId(@Param("communityId") Integer communityId);

}
