package finalProj.domain.notifications;

import java.time.LocalDateTime;

import finalProj.domain.users.Units;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "units_notifications")
public class UnitsNotifications {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "units_notifications_id")
	private Integer unitsNotificationsId;

	@ManyToOne
	@JoinColumn(name = "notifications_id", nullable = false)
	private Notifications notifications;

	@ManyToOne
	@JoinColumn(name = "units_id", nullable = false)
	private Units units;

	@Column(name = "is_read")
	private Integer isRead = 0; // 預設未讀

	@Column(name = "read_time")
	private LocalDateTime readTime;

	public Integer getUnitsNotificationsId() {
		return unitsNotificationsId;
	}

	public void setUnitsNotificationsId(Integer unitsNotificationsId) {
		this.unitsNotificationsId = unitsNotificationsId;
	}

	public Notifications getNotifications() {
		return notifications;
	}

	public void setNotifications(Notifications notifications) {
		this.notifications = notifications;
	}

	public Units getUnits() {
		return units;
	}

	public void setUnits(Units units) {
		this.units = units;
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
		return "UnitsNotifications [unitsNotificationsId=" + unitsNotificationsId + ", notifications=" + notifications
				+ ", units=" + units + ", isRead=" + isRead + ", readTime=" + readTime + "]";
	}

}
