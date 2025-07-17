package finalProj.repository.notifications;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.notifications.UnitsNotifications;

@Repository
public interface UnitsNotificationsRepository extends JpaRepository<UnitsNotifications, Integer> {
    List<UnitsNotifications> findByUnitsUnitsId(Integer unitId);

    List<UnitsNotifications> findByUnitsUnitsIdAndIsRead(Integer unitId, Integer isRead);

}
