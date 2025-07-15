package finalProj.repository.notifications;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.notifications.UsersNotifications;

@Repository
public interface UsersNotificationsRepository extends JpaRepository<UsersNotifications, Integer> {

}
