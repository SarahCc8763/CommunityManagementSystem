package finalProj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.TemporaryParkingApplication;

public interface TemporaryParkingApplicationRepository extends JpaRepository<TemporaryParkingApplication, Integer> {

	List<TemporaryParkingApplication> findByParkingSlotId(Integer parkingSlotId);
}
