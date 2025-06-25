package finalProj.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// 抽籤申請表
@Entity
@Table(name = "lottery_apply"
//, uniqueConstraints = {
//    @UniqueConstraint(columnNames = {"lottery_events_id", "users_id"})}
)
public class LotteryApply {

	// 流水號
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	// 申請人 Id
	@Column(name = "users_id", nullable = false)
	private Integer usersId;

	// 活動 Id
	@Column(name = "lottery_events_id", nullable = false)
	private Integer lotteryEventsId;

	// 申請日期
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Taipei")
	@Column(name = "applied_at")
	private Date appliedAt;

	// 中獎車位
	@Column(name = "lottery_event_spaces_id")
	private Integer lotteryEventSpacesId;

	@Override
	public String toString() {
		return "LotteryApply [id=" + id + ", usersId=" + usersId + ", lotteryEventsId=" + lotteryEventsId
				+ ", appliedAt=" + appliedAt + ", lotteryEventSpacesId=" + lotteryEventSpacesId + "]";
	}

	// Getters and Setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUsersId() {
		return usersId;
	}

	public void setUsersId(Integer usersId) {
		this.usersId = usersId;
	}

	public Integer getLotteryEventsId() {
		return lotteryEventsId;
	}

	public void setLotteryEventsId(Integer lotteryEventsId) {
		this.lotteryEventsId = lotteryEventsId;
	}

	public Date getAppliedAt() {
		return appliedAt;
	}

	public void setAppliedAt(Date appliedAt) {
		this.appliedAt = appliedAt;
	}

	public Integer getLotteryEventSpacesId() {
		return lotteryEventSpacesId;
	}

	public void setLotteryEventSpacesId(Integer lotteryEventSpacesId) {
		this.lotteryEventSpacesId = lotteryEventSpacesId;
	}
}
