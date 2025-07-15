package finalProj.service.facilities.process.reserveWithPointDeduction;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.facilities.FacilitiesBean;
import finalProj.domain.facilities.FacilityReservationsBean;
import finalProj.domain.facilities.PointAccountsBean;
import finalProj.dto.facilities.reservations.ReservationRequest;
import finalProj.service.facilities.FacilityReservationsService;
import finalProj.service.facilities.PointAccountsService;

@Service
public class ReservationCreator {
	@Autowired
	private FacilityReservationsService facilityReservationsService;

	@Autowired
	private PointAccountsService pointAccountsService;

	/**
	 * 根據預約請求與公設資訊，建立一筆預約資料並儲存
	 * 
	 * @param request  預約請求 DTO
	 * @param facility 驗證後的公設 Entity
	 * @return 儲存後的預約物件（含 ID）
	 */
	public FacilityReservationsBean createReservation(ReservationRequest request, FacilitiesBean facility) {
		// 取得帳戶與對應的住戶單位
		PointAccountsBean account = pointAccountsService.findById(request.getAccountId());

		FacilityReservationsBean reservation = new FacilityReservationsBean();
		reservation.setFacility(facility);
		reservation.setUnit(account.getUnit());
		reservation.setCommunity(facility.getCommunity());
		reservation.setNumberOfUsers(request.getNumberOfUsers());
		reservation.setReservationStartTime(request.getReservationStartTime());
		reservation.setReservationEndTime(request.getReservationEndTime());
		reservation.setIsAdmin(request.getIsAdmin());
		reservation.setRequiredPoints(request.getDeductAmount());
		reservation.setActualUsedPoints(
				request.getIsAdmin() != null && request.getIsAdmin() ? 0 : request.getDeductAmount());
		reservation.setRemark(request.getRemark());
		reservation.setReservationStatus("APPROVED");
		reservation.setCreatedAt(LocalDateTime.now());
		reservation.setPointExpireAt(account.getExpiredAt());
    	
		return facilityReservationsService.create(reservation);
	}
}
