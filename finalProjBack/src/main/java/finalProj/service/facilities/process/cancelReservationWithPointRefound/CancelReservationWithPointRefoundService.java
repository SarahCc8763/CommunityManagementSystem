package finalProj.service.facilities.process.cancelReservationWithPointRefound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.facilities.FacilityReservationsBean;
import finalProj.dto.facilities.reservations.CancelReservationRequest;

@Service
@Transactional
public class CancelReservationWithPointRefoundService {

	@Autowired
	private PointRefundProcessor pointRefundProcessor;
	
	@Autowired
	private CancelReservationValidator cancelReservationValidator;
	
	@Autowired
	private ReservationCanceler reservationCanceler;

	public void cancelReservationAndRefund(CancelReservationRequest request) {
		
		
		FacilityReservationsBean reservation = cancelReservationValidator.validate(request.getReservationId());
		
		//先抓出要返點的值，之後點數返點要代入
		int originalPoints = reservation.getActualUsedPoints();

		reservation = reservationCanceler.cancel(reservation, request.getCancelReason());

		pointRefundProcessor.refundIfEligible(reservation, originalPoints);


	}
}
