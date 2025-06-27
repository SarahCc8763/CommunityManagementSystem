package finalProj.repository.parking;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import finalProj.domain.parking.ParkingSlot;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Integer> {

	boolean existsBySlotNumber(String slotNumber);

	@Query(value = """
			SELECT *
			FROM parking_slot ps
			WHERE ps.parking_type_id = :typeId
			  AND ps.users_id = 1
			  AND ps.is_rentable = 1
			  AND NOT EXISTS (
			    SELECT 1
			    FROM parking_rentals pr
			    WHERE pr.parking_slot_id = ps.id
			      AND pr.rent_buy_start <= :eventEnd
			      AND pr.rent_end >= :eventStart
			  )
			""", nativeQuery = true)
	List<ParkingSlot> findAvailableSlotsForEvent(@Param("typeId") Integer typeId, @Param("eventStart") Date eventStart,
			@Param("eventEnd") Date eventEnd, Pageable pageable);

	List<ParkingSlot> findByCommunityId(Integer communityId);


}
