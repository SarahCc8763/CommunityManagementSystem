package finalProj.service.notifications;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.community.Community;
import finalProj.domain.notifications.Notifications;
import finalProj.domain.notifications.UnitsNotifications;
import finalProj.domain.packages.Packages;
import finalProj.domain.users.Units;
import finalProj.repository.notifications.NotificationsRepository;
import finalProj.repository.notifications.UnitsNotificationsRepository;

@Service
@Transactional
public class NotificationsService {

    @Autowired
    NotificationsRepository notificationsRepository;

    @Autowired
    UnitsNotificationsRepository unitsNotificationsRepository;

    // @Autowired
    // UsersNotificationsRepository usersNotificationsRepository;

    // @Autowired
    // NotificationsWebSocketService notificationsWebSocketService;

    public Notifications createPackageNotification(Packages packages) {
        Units unit = packages.getUnit();
        Community community = packages.getCommunity();

        // === 建立一筆 Notifications ===
        Notifications notification = new Notifications();
        notification.setTitle("您有" + packages.getType() + "待領取");
        notification.setDescription("門牌號：" + unit.getUnit() + " 號 " + unit.getFloor() + " 有新" + packages.getType() + " "
                + packages.getPiece() + " 件" + "，請盡快領取！");
        notification.setCreatedTime(LocalDateTime.now());
        notification.setCommunity(community);
        notificationsRepository.save(notification);

        // === 建立 UnitsNotifications ===
        UnitsNotifications unitsNotifications = new UnitsNotifications();
        unitsNotifications.setNotifications(notification);
        unitsNotifications.setUnits(unit);
        unitsNotificationsRepository.save(unitsNotifications);

        // === 建立 UsersNotifications 給該 Unit 下所有住戶 ===
        // List<UnitsUsers> unitsUsersList = unit.getUnitsUsersList();
        // if (unitsUsersList != null && !unitsUsersList.isEmpty()) {
        // for (UnitsUsers unitsUsers : unitsUsersList) {
        // Users user = unitsUsers.getUser();
        //
        // UsersNotifications usersNotifications = new UsersNotifications();
        // usersNotifications.setNotifications(notification);
        // usersNotifications.setUser(user);
        // usersNotifications.setIsRead(0); // 預設未讀
        // usersNotificationsRepository.save(usersNotifications);
        //
        // }
        // 即時推送
        // notificationsWebSocketService.sendToUnit(unit.getUnitsId(),
        // notification.getTitle(),
        // notification.getDescription());

        // }

        return notification;
    }

}
