package finalProj.repository.parking;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import finalProj.domain.parking.ParkingRentals;
import finalProj.domain.parking.ParkingSlot;

public interface ParkingRentalsRepository extends JpaRepository<ParkingRentals, Integer> {

	List<ParkingRentals> findByParkingSlotId(Integer parkingSlotId);
	
	@Query("""
		    SELECT ps
		    FROM ParkingSlot ps
		    WHERE ps.parkingTypeId = :parkingTypeId
		      AND ps.isRentable = true
		      AND ps.id NOT IN (
		        SELECT pr.parkingSlotId
		        FROM ParkingRentals pr
		        WHERE (
		          pr.rentBuyStart <= :end AND pr.rentEnd >= :start
		        )
		      )
		""")
		List<ParkingSlot> findAvailableSlotsByTypeAndPeriod(
		    @Param("parkingTypeId") Integer parkingTypeId,
		    @Param("start") Date start,
		    @Param("end") Date end
		);

}
