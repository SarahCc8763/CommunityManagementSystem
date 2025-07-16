package finalProj.service.facilities.process.cancelReservationWithPointRefound;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.facilities.FacilityReservationsBean;
import finalProj.repository.facilities.FacilityReservationsRepository;

@Service
public class ReservationCanceler {
	@Autowired
	private FacilityReservationsRepository facilityReservationsRepository;

	/**
	 * 將預約狀態設為已取消，並更新相關欄位
	 */
	public FacilityReservationsBean cancel(FacilityReservationsBean reservation, String cancelReason) {
		reservation.setReservationStatus("CANCELLED");
		reservation.setCancelReason(cancelReason);
		reservation.setCanceledAt(LocalDateTime.now());
		reservation.setActualUsedPoints(0); // 點數清為 0
		reservation.setUpdatedAt(LocalDateTime.now());

		return facilityReservationsRepository.save(reservation);
	}
}
