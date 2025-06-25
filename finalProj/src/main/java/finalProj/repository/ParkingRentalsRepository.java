package finalProj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.ParkingRentals;

public interface ParkingRentalsRepository extends JpaRepository<ParkingRentals, Integer> {

	List<ParkingRentals> findByParkingSlotId(Integer parkingSlotId);
}
