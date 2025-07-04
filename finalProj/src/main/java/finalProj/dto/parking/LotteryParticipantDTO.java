package finalProj.dto.parking;

import java.util.Date;

public class LotteryParticipantDTO {
	
	private String userName;
	
	private Date appliedAt;
	
	private String parkingSlots;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getAppliedAt() {
		return appliedAt;
	}

	public void setAppliedAt(Date appliedAt) {
		this.appliedAt = appliedAt;
	}

	public String getParkingSlots() {
		return parkingSlots;
	}

	public void setParkingSlots(String parkingSlots) {
		this.parkingSlots = parkingSlots;
	}
	
}
