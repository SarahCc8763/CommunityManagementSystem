package finalProj.service.facilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import finalProj.domain.facilities.FacilityReservationsBean;
import finalProj.dto.facilities.reservations.ReservationRecordDTO;

public interface FacilityReservationsService {

	FacilityReservationsBean create(FacilityReservationsBean reservation); // 新增

	FacilityReservationsBean update(FacilityReservationsBean reservation); // 修改

	FacilityReservationsBean findById(Integer id); // 查單筆

	List<FacilityReservationsBean> findByUnitId(Integer unitId); // 某住戶全部預約

	List<FacilityReservationsBean> findByFacilityIdAndDate(Integer facilityId, LocalDate date); // 某日某設施全部預約
	
	List<ReservationRecordDTO> findRecordsByUnitId(Integer unitId);

	boolean hasConflict(
			Integer facilityId,
			LocalDateTime startTime,
			LocalDateTime endTime,
			Integer userCount
	);
	

	void validateReservationConflict(
			Integer facilityId,
			LocalDateTime startTime,
			LocalDateTime endTime,
			Integer userCount
	);

}
