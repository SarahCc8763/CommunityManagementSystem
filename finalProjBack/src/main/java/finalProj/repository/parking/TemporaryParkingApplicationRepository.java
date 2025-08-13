package finalProj.repository.parking;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.parking.TemporaryParkingApplication;

public interface TemporaryParkingApplicationRepository extends JpaRepository<TemporaryParkingApplication, Integer> {

	List<TemporaryParkingApplication> findByParkingSlotId(Integer parkingSlotId);

	List<TemporaryParkingApplication> findByParkingSlot_Community_CommunityId(Integer communityId);
}
