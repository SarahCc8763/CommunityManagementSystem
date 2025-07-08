package finalProj.repository.parking;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import finalProj.domain.community.Community;
import finalProj.domain.parking.ParkingSlot;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Integer> {

	boolean existsBySlotNumber(String slotNumber);

	@Query("""
			    SELECT ps FROM ParkingSlot ps
			    WHERE ps.community.id = :communityId
				  AND ps.parkingType.id = :typeId
			      AND ps.users.name = 'sa'
			      AND ps.isRentable = true
			      AND NOT EXISTS (
			        SELECT 1 FROM ParkingRentals pr
			        WHERE pr.parkingSlot.id = ps.id
			          AND pr.rentBuyStart <= :eventEnd
			          AND pr.rentEnd >= :eventStart
			      )
			""")
	List<ParkingSlot> findAvailableSlotsForEvent(
			@Param("communityId") Integer communityId,
			@Param("typeId") Integer typeId,
			@Param("eventStart") Date eventStart,
			@Param("eventEnd") Date eventEnd,
			Pageable pageable);

	@Query("""
			  SELECT s FROM ParkingSlot s
			  WHERE s.community.communityId = :communityId
			    AND s.parkingType.id = :typeId
			    AND s.isRentable = false
			    AND NOT EXISTS (
			      SELECT 1 FROM TemporaryParkingApplication t
			      WHERE t.parkingSlot = s
			        AND NOT (
			          t.endTime <= :startTime OR t.startTime >= :endTime
			        )
			    )
			""")
	List<ParkingSlot> findAvailableTempSlots(
			@Param("communityId") Integer communityId,
			@Param("typeId") Integer typeId,
			@Param("startTime") Date start,
			@Param("endTime") Date end);

	List<ParkingSlot> findByCommunity_CommunityId(Integer communityId);

	boolean existsByCommunityAndSlotNumber(Community community, String slotNumber);

	Collection<ParkingSlot> findByCommunity(Community community);

	ParkingSlot findBySlotNumberAndCommunity_CommunityId(String slotNumber, Integer communityId);

	List<ParkingSlot> findByUsers_UsersId(Integer usersId);

}
