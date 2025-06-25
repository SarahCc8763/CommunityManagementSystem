package finalProj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.LotteryEvents;

public interface LotteryEventRepository extends JpaRepository<LotteryEvents, Integer> {
}
