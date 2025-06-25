package finalProj.repository.parking;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.parking.ParkingType;

public interface ParkingTypeRepository extends JpaRepository<ParkingType, Integer> {

	boolean existsByType(String type);
}
