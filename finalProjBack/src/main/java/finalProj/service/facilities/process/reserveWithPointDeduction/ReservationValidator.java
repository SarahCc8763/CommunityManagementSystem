package finalProj.service.facilities.process.reserveWithPointDeduction;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.facilities.FacilitiesBean;
import finalProj.domain.facilities.PointAccountsBean;
import finalProj.dto.facilities.reservations.ReservationRequest;
import finalProj.exception.facilities.InsufficientPointsException;
import finalProj.exception.facilities.InvalidReservationException;
import finalProj.exception.facilities.ResourceNotFoundException;
import finalProj.service.facilities.FacilitiesService;
import finalProj.service.facilities.FacilityReservationsService;
import finalProj.service.facilities.PointAccountsService;

@Service
public class ReservationValidator {

	@Autowired
	private FacilitiesService facilitiesService;

	@Autowired
	private FacilityReservationsService facilityReservationsService;

	@Autowired
	private PointAccountsService pointAccountsService;

	public FacilitiesBean validate(ReservationRequest request) {

		// 1. 確認公設存在
		FacilitiesBean facility = facilitiesService.findById(request.getFacilityId());
		if (facility == null) {
			throw new ResourceNotFoundException("找不到指定的公設");
		}

		// 2-1. 預約時間格式正確
		if (!request.getReservationEndTime().isAfter(request.getReservationStartTime())) {
			throw new InvalidReservationException("結束時間必須晚於開始時間");
		}
		
		// 2-2. 預約開始時間不可早於現在
		if (!request.getReservationStartTime().isAfter(LocalDateTime.now())) {
		    throw new InvalidReservationException("預約開始時間必須晚於現在時間");
		}

		// 2-3. 預約結束時間不可超過 15 天後的 23:59:59
		LocalDateTime latestAllowed = LocalDateTime.now().plusDays(15).with(LocalTime.MAX);
		if (request.getReservationEndTime().isAfter(latestAllowed)) {
		    throw new InvalidReservationException("預約時間不可超過 15 天");
		}

		// 3. 預約是否在開放時間內
		LocalTime openTime = facility.getOpenTime();
		LocalTime closeTime = facility.getCloseTime();
		LocalTime startTime = request.getReservationStartTime().toLocalTime();
		LocalTime endTime = request.getReservationEndTime().toLocalTime();

		boolean is24Hours = openTime.equals(LocalTime.MIN);
		boolean isOutOfRange = startTime.isBefore(openTime) || endTime.isAfter(closeTime);

		if (!is24Hours && isOutOfRange) {
		    throw new InvalidReservationException("預約時間不在開放時段內");
		}

		// 4. 預約時長不得超過限制
		long durationMinutes = Duration.between(request.getReservationStartTime(), request.getReservationEndTime())
				.toMinutes();
		if (durationMinutes > facility.getReservableDuration()) {
			throw new InvalidReservationException("預約時長不得超過 " + facility.getReservableDuration() + " 分鐘");
		}

		// 5. 是否有衝突		
		facilityReservationsService.validateReservationConflict(
				request.getFacilityId(),
				request.getReservationStartTime(), 
				request.getReservationEndTime(), 
				request.getNumberOfUsers()
				);

		// 6. 點數是否足夠（非管理員）
		boolean isAdmin = Boolean.TRUE.equals(request.getIsAdmin());
		if (!isAdmin) {
			PointAccountsBean account = pointAccountsService.findById(request.getAccountId());
			if (account.getTotalBalance() < request.getDeductAmount()) {
				throw new InsufficientPointsException("點數不足，無法預約");
			}
		}

		return facility;
	}
}
