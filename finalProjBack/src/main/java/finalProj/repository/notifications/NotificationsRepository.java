package finalProj.repository.notifications;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.notifications.Notifications;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, Integer> {

}
