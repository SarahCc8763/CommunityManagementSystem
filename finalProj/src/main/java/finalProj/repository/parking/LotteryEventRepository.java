package finalProj.repository.parking;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.parking.LotteryEvents;

public interface LotteryEventRepository extends JpaRepository<LotteryEvents, Integer> {
}
