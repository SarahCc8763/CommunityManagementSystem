package finalProj.repository.notifications;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.notifications.UnitsNotifications;

@Repository
public interface UnitsNotificationsRepository extends JpaRepository<UnitsNotifications, Integer> {

}
