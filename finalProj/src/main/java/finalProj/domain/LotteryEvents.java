package finalProj.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// 抽籤活動
@Entity
@Table(name = "lottery_events")
public class LotteryEvents {

	// 流水號
	@Id
	@Column(name = "id")
	private Integer id;

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

	// 創建人
	@Column(name = "users_id", nullable = false)
	private Integer usersId;

	// 創建時間
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	@Column(name = "created_at", nullable = false)
	private Date createdAt;

	@Override
	public String toString() {
		return "LotteryEvents [id=" + id + ", title=" + title + ", startedAt=" + startedAt + ", endedAt=" + endedAt
				+ ", usersId=" + usersId + ", createdAt=" + createdAt + "]";
	}

	// Getters and Setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getUsersId() {
		return usersId;
	}

	public void setUsersId(Integer usersId) {
		this.usersId = usersId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
