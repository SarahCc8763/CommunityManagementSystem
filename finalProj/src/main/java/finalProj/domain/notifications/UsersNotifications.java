package finalProj.domain.notifications;

import java.time.LocalDateTime;

import finalProj.domain.users.Users;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users_notifications")
public class UsersNotifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_notifications_id")
    private Integer usersNotificationsId;

    @ManyToOne
    @JoinColumn(name = "notifications_id", nullable = false)
    private Notifications notifications;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private Users user;

    @Column(name = "is_read")
    private Integer isRead = 0;

    @Column(name = "read_time")
    private LocalDateTime readTime;

	public Integer getUsersNotificationsId() {
		return usersNotificationsId;
	}

	public void setUsersNotificationsId(Integer usersNotificationsId) {
		this.usersNotificationsId = usersNotificationsId;
	}

	public Notifications getNotifications() {
		return notifications;
	}

	public void setNotifications(Notifications notifications) {
		this.notifications = notifications;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	public LocalDateTime getReadTime() {
		return readTime;
	}

	public void setReadTime(LocalDateTime readTime) {
		this.readTime = readTime;
	}

	@Override
	public String toString() {
		return "UsersNotifications [usersNotificationsId=" + usersNotificationsId + ", notifications=" + notifications
				+ ", user=" + user + ", isRead=" + isRead + ", readTime=" + readTime + "]";
	}

	

    
    
}
