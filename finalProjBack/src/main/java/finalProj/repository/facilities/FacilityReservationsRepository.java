package finalProj.repository.facilities;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import finalProj.domain.facilities.FacilityReservationsBean;

@Repository
public interface FacilityReservationsRepository extends JpaRepository<FacilityReservationsBean, Integer> {
	

	List<FacilityReservationsBean> findByFacilityFacilityIdAndReservationStartTimeBetween(
		    Integer facilityId,
		    LocalDateTime start,
		    LocalDateTime end);
	
	// 查詢某公設在某段時間內的預約(status = "APPROVED")（避免衝突）	
	List<FacilityReservationsBean> findByFacilityFacilityIdAndReservationStartTimeBetweenAndReservationStatus(
		    Integer facilityId,
		    LocalDateTime start,
		    LocalDateTime end,
		    String status);
	                               

	// 查住戶的所有預約
	List<FacilityReservationsBean> findByUnit_UnitsId(Integer unitId);
	
	
	@Query("SELECT f.maxUsers FROM FacilitiesBean f WHERE f.facilityId = :facilityId")
	Integer findFacilityMaxUsers(@Param("facilityId") Integer facilityId);
	
	@Query("SELECT r FROM FacilityReservationsBean r JOIN FETCH r.facility WHERE r.unit.unitsId = :unitsId")
	List<FacilityReservationsBean> findByUnitIdWithFacility(@Param("unitsId") Integer unitsId);
	
	
	@Query("SELECT COALESCE(SUM(r.numberOfUsers), 0) FROM FacilityReservationsBean r " +
		       "WHERE r.facility.facilityId = :facilityId " +
		       "AND r.reservationStatus = 'APPROVED' " +
		       "AND r.reservationEndTime > :startTime " +
		       "AND r.reservationStartTime < :endTime")
	int countTotalUsersInTimeRange(@Param("facilityId") Integer facilityId,
		                           @Param("startTime") LocalDateTime startTime,
		                           @Param("endTime") LocalDateTime endTime);

	@Query("SELECT COUNT(r) FROM FacilityReservationsBean r " +
				"WHERE r.facility.facilityId = :facilityId " +
				"AND r.reservationStatus = 'APPROVED' " +
				"AND r.reservationEndTime > :startTime " +
		       	"AND r.reservationStartTime < :endTime")
	int countTimeConflicts(@Param("facilityId") Integer facilityId,
		                   @Param("startTime") LocalDateTime startTime,
		                   @Param("endTime") LocalDateTime endTime);
	
	@Query("""
		    SELECT r
		    FROM FacilityReservationsBean r
		    WHERE r.facility.facilityId = :facilityId
		      AND r.reservationStatus = 'APPROVED'
		      AND r.reservationStartTime < :endTime
		      AND r.reservationEndTime > :startTime
		""")
		List<FacilityReservationsBean> findOverlappingApprovedReservations(
		    @Param("facilityId") Integer facilityId,
		    @Param("startTime") LocalDateTime startTime,
		    @Param("endTime") LocalDateTime endTime
		);
}
