package finalProj.service.facilities.process.reserveWithPointDeduction;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.facilities.FacilitiesBean;
import finalProj.domain.facilities.FacilityReservationsBean;
import finalProj.domain.facilities.PointAccountsBean;
import finalProj.domain.facilities.PointTransactionsBean;
import finalProj.domain.notifications.Notifications;
import finalProj.domain.notifications.UnitsNotifications;
import finalProj.dto.facilities.reservations.ReservationRequest;
import finalProj.exception.facilities.InsufficientPointsException;
import finalProj.repository.notifications.NotificationsRepository;
import finalProj.repository.notifications.UnitsNotificationsRepository;
import finalProj.service.facilities.PointAccountsService;
import finalProj.service.facilities.PointTransactionsService;

@Service
@Transactional
public class ReservationProcessingService {

	@Autowired
	private ReservationValidator reservationValidator;

	@Autowired
	private ReservationCreator reservationCreator;

	@Autowired
	private PointAccountsService pointAccountsService;

	@Autowired
	private PointTransactionsService pointTransactionsService;

	@Autowired
    NotificationsRepository notificationsRepository;

    @Autowired
    UnitsNotificationsRepository unitsNotificationsRepository;

	/**
	 * 執行預約流程：驗證 → 建立預約 → 扣點 → 建立交易紀錄
	 */
	public void reserveWithPointDeduction(ReservationRequest request) {
		// Step 1: 驗證預約是否合法（並取得公設資料）
		FacilitiesBean facility = reservationValidator.validate(request);
		
		// Step 2: 建立預約
		FacilityReservationsBean reservation = reservationCreator.createReservation(request, facility);
		
    
		// Step 3: 扣除點數（若非 admin）
		boolean isAdmin = request.getIsAdmin() != null && request.getIsAdmin();
		int deductAmount = isAdmin ? 0 : request.getDeductAmount();

		PointAccountsBean account = pointAccountsService.findById(request.getAccountId());

		if (!isAdmin && account.getTotalBalance() < deductAmount) {
			throw new InsufficientPointsException("點數不足，無法預約");
		}

		pointAccountsService.decreasePoints(account, deductAmount);

		// Step 4: 建立一筆點數異動紀錄
		if (!isAdmin && deductAmount > 0) {
			PointTransactionsBean txn = new PointTransactionsBean();
			txn.setCommunity(account.getUnit().getCommunity());
			txn.setUnit(account.getUnit());
			txn.setTransactionType("RESERVATION");
			txn.setAmount(-deductAmount);
			txn.setReservation(reservation);
			txn.setTransactionDescription("預約 " + facility.getFacilityName() + " 扣點");
			txn.setCreatedAt(LocalDateTime.now());
			pointTransactionsService.create(txn);
		}

		// === 建立預約扣點 Notifications ===
		Notifications notification = new Notifications();
		notification.setTitle("您已成功預約 " + request.getReservationStartTime() + facility.getFacilityName());
		notification.setDescription("您已成功預約 " + facility.getFacilityName() + "，扣點" + deductAmount + "點");	
		notification.setCreatedTime(LocalDateTime.now());
		notification.setCommunity(account.getUnit().getCommunity());
		notificationsRepository.save(notification);
		
		UnitsNotifications unitsNotifications = new UnitsNotifications();
		unitsNotifications.setNotifications(notification);
		unitsNotifications.setUnits(account.getUnit());
		unitsNotificationsRepository.save(unitsNotifications);
	}
}
