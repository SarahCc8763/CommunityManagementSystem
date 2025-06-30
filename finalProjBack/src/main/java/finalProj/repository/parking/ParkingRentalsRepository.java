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
			    WHERE ps.community.id = :communityId
			      AND ps.parkingType.id = :parkingTypeId
			      AND ps.isRentable = true
			      AND ps.id NOT IN (
			        SELECT pr.parkingSlot.id
			        FROM ParkingRentals pr
			        WHERE (
			          pr.rentBuyStart <= :end AND pr.rentEnd >= :start
			        )
			      )
			""")
	List<ParkingSlot> findAvailableSlotsByTypeAndPeriod(
			@Param("communityId") Integer communityId,
			@Param("parkingTypeId") Integer parkingTypeId, 
			@Param("start") Date start, 
			@Param("end") Date end);

	@Query("""
			    SELECT r FROM ParkingRentals r
			    WHERE r.community.id = :communityId
				  AND r.parkingSlot.id = :slotId
			      AND (:startDate IS NULL OR r.rentBuyStart >= :startDate)
			""")
	List<ParkingRentals> findHistoryBySlotIdAndStartDate(
			@Param("communityId") Integer communityId,
		    @Param("slotId") Integer slotId,
			@Param("startDate") Date startDate);

	List<ParkingRentals> findByCommunity_CommunityId(Integer communityId);

}
