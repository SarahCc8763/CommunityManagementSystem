package finalProj.dto.parking;

import java.util.Date;

public class LotteryApplyDTO {
	private Integer id;
	private Integer usersId;
	private String userName;
	private String email;
	private Integer lotteryEventId;
	private String eventTitle;
	private Date appliedAt;
	private String awardedSlot; // 若中籤則填入車位號碼，否則為 null

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getLotteryEventId() {
		return lotteryEventId;
	}

	public void setLotteryEventId(Integer lotteryEventId) {
		this.lotteryEventId = lotteryEventId;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public Date getAppliedAt() {
		return appliedAt;
	}

	public void setAppliedAt(Date appliedAt) {
		this.appliedAt = appliedAt;
	}

	public String getAwardedSlot() {
		return awardedSlot;
	}

	public void setAwardedSlot(String awardedSlot) {
		this.awardedSlot = awardedSlot;
	}

}
