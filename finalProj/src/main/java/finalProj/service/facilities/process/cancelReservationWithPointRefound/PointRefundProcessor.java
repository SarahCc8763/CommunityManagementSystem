package finalProj.service.facilities.process.cancelReservationWithPointRefound;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.facilities.FacilityReservationsBean;
import finalProj.domain.facilities.PointAccountsBean;
import finalProj.domain.facilities.PointTransactionsBean;
import finalProj.domain.notifications.Notifications;
import finalProj.domain.notifications.UnitsNotifications;
import finalProj.repository.notifications.NotificationsRepository;
import finalProj.repository.notifications.UnitsNotificationsRepository;
import finalProj.service.facilities.PointAccountsService;
import finalProj.service.facilities.PointTransactionsService;

@Service
public class PointRefundProcessor {
	
	@Autowired
	private PointAccountsService pointAccountsService;

	@Autowired
	private PointTransactionsService pointTransactionsService;

    @Autowired
    NotificationsRepository notificationsRepository;

    @Autowired
    UnitsNotificationsRepository unitsNotificationsRepository;
	
	public void refundIfEligible(FacilityReservationsBean reservation, int originalPoints) {
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireAt = reservation.getPointExpireAt();

        boolean refundable = (expireAt != null && expireAt.isAfter(now));
        int refundAmount = refundable ? originalPoints : 0;

        if (refundAmount > 0) {
            // 取得帳戶資料（透過單位找帳戶）
            PointAccountsBean account = reservation.getUnit().getPointAccount(); // 假設你有設好 OneToOne

            // 回補點數
			pointAccountsService.increasePoints(account, refundAmount, reservation.getPointExpireAt());

			String facilityName = reservation.getFacility().getFacilityName();
            // 建立交易紀錄
            PointTransactionsBean txn = new PointTransactionsBean();
            txn.setCommunity(reservation.getUnit().getCommunity());
            txn.setUnit(reservation.getUnit());
            txn.setReservation(reservation);
            txn.setTransactionType("CANCEL");
            txn.setAmount(refundAmount); // 正值
            txn.setTransactionDescription("取消「" + facilityName + "」返還點數");
            txn.setCreatedAt(LocalDateTime.now());

            pointTransactionsService.create(txn);

            // === 建立取消反點 Notifications ===
            Notifications notification = new Notifications();
            notification.setTitle("您已成功取消 " + reservation.getReservationEndTime() + facilityName);
            notification.setDescription("您已成功取消 " + facilityName + "，反點" + refundAmount + "點");	
            notification.setCreatedTime(LocalDateTime.now());
            notification.setCommunity(account.getUnit().getCommunity());
            notificationsRepository.save(notification);
            
            UnitsNotifications unitsNotifications = new UnitsNotifications();
            unitsNotifications.setNotifications(notification);
            unitsNotifications.setUnits(account.getUnit());
            unitsNotificationsRepository.save(unitsNotifications);
        }

    }
	
	
	
}
