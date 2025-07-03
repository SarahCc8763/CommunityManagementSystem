package finalProj.domain.parking;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import finalProj.domain.users.Users;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

// 抽籤申請表
@Entity
@Table(name = "lottery_apply")
public class LotteryApply {

	// 流水號
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	// 多對一到申請人
	@JsonBackReference("users-lotteryApply")
	@ManyToOne
	@JoinColumn(name = "users_id", referencedColumnName = "users_id", nullable = false)
	private Users users;

	// 多對一到抽籤活動
	@JsonBackReference("lotteryEvents-lotteryApply")
	@ManyToOne
	@JoinColumn(name = "lottery_events_id", referencedColumnName = "bulletin_id", nullable = false)
	private LotteryEvents lotteryEvents;
	
	// 抽籤活動 Id
	@Transient
	private Integer lotteryEventsId;

	// 多對一到中獎車位
	@JsonBackReference("lotteryEventSpace-lotteryApply")
	@ManyToOne
	@JoinColumn(name = "lottery_event_spaces_id", referencedColumnName = "id")
	private LotteryEventSpace lotteryEventSpace;

	// 申請日期
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Taipei")
	@Column(name = "applied_at")
	private Date appliedAt;

	@Override
	public String toString() {
		return "LotteryApply [id=" + id + ", users=" + users + ", lotteryEvents=" + lotteryEvents
				+ ", lotteryEventSpace=" + lotteryEventSpace + ", appliedAt=" + appliedAt + ", lotteryEventsId=" + lotteryEventsId + "]";
	}

	public Integer getLotteryEventsId() {
		return lotteryEvents.getBulletinId();
	}

	public void setLotteryEventsId(Integer lotteryEventsId) {
		this.lotteryEventsId = lotteryEventsId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public LotteryEvents getLotteryEvents() {
		return lotteryEvents;
	}

	public void setLotteryEvents(LotteryEvents lotteryEvents) {
		this.lotteryEvents = lotteryEvents;
	}

	public LotteryEventSpace getLotteryEventSpace() {
		return lotteryEventSpace;
	}

	public void setLotteryEventSpace(LotteryEventSpace lotteryEventSpace) {
		this.lotteryEventSpace = lotteryEventSpace;
	}

	public Date getAppliedAt() {
		return appliedAt;
	}

	public void setAppliedAt(Date appliedAt) {
		this.appliedAt = appliedAt;
	}

}
