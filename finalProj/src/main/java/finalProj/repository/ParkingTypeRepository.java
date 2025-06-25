package finalProj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.ParkingType;

public interface ParkingTypeRepository extends JpaRepository<ParkingType, Integer> {

	boolean existsByType(String type);
}
