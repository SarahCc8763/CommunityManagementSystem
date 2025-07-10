package finalProj.domain.parking;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import finalProj.domain.bulletin.Bulletin;
import finalProj.domain.users.Users;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

// 抽籤活動
@Entity
@Table(name = "lottery_events")
public class LotteryEvents {

	// 流水號
	@Id
	@Column(name = "bulletin_id")
	private Integer bulletinId;

	@OneToOne
	@MapsId // 指 bulletinId 是關聯的主鍵
	@JoinColumn(name = "bulletin_id")
	@JsonBackReference("bulletin-lotteryEevents")
	private Bulletin bulletin;

	// 多對一到創建人
	@JsonBackReference("users-lotteryEevents")
	@ManyToOne
	@JoinColumn(name = "users_id", referencedColumnName = "users_id", nullable = false)
	private Users users;

	// 多對一到車位種類
	@JsonBackReference("parkingType-lotteryEevents")
	@ManyToOne
	@JoinColumn(name = "parking_type_id", referencedColumnName = "id")
	private ParkingType parkingType;

	// 標題
	@Column(name = "title", nullable = false, length = 50)
	private String title;

	// 開始時間
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	@Column(name = "started_at", nullable = false)
	private Date startedAt;

	// 結束時間
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	@Column(name = "ended_at", nullable = false)
	private Date endedAt;

	// 創建時間
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	@Column(name = "created_at", nullable = false)
	private Date createdAt;

	@Column(name = "status", nullable = false)
	private Boolean status;

	// ---------------------------------------------------------------------------------------

	// 一對多到抽籤車位
	@JsonManagedReference("lotteryEvents-lotteryEventSpace")
	@OneToMany(mappedBy = "lotteryEvents", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LotteryEventSpace> lotteryEventSpace;

	// 一對多到抽籤申請
	@JsonManagedReference("lotteryEvents-lotteryApply")
	@OneToMany(mappedBy = "lotteryEvents", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LotteryApply> lotteryApply;

	// ---------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "LotteryEvents [bulletinId=" + bulletinId + ", bulletin=" + bulletin + ", users=" + users
				+ ", parkingType=" + parkingType + ", title=" + title + ", startedAt=" + startedAt + ", endedAt="
				+ endedAt + ", createdAt=" + createdAt + ", status=" + status + "]";
	}

	public Integer getBulletinId() {
		return bulletinId;
	}

	public void setBulletinId(Integer bulletinId) {
		this.bulletinId = bulletinId;
	}

	public Bulletin getBulletin() {
		return bulletin;
	}

	public void setBulletin(Bulletin bulletin) {
		this.bulletin = bulletin;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}

	public Date getEndedAt() {
		return endedAt;
	}

	public void setEndedAt(Date endedAt) {
		this.endedAt = endedAt;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public List<LotteryEventSpace> getLotteryEventSpace() {
		return lotteryEventSpace;
	}

	public void setLotteryEventSpace(List<LotteryEventSpace> lotteryEventSpace) {
		this.lotteryEventSpace = lotteryEventSpace;
	}

	public List<LotteryApply> getLotteryApply() {
		return lotteryApply;
	}

	public void setLotteryApply(List<LotteryApply> lotteryApply) {
		this.lotteryApply = lotteryApply;
	}

	public ParkingType getParkingType() {
		return parkingType;
	}

	public void setParkingType(ParkingType parkingType) {
		this.parkingType = parkingType;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}
