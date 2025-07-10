package finalProj.dto.parking;

public class WinnerDTO {
	private Integer userId;
	private Integer applyId;
	private Integer parkingSlotId;
	private String slotNumber;

	public WinnerDTO(Integer userId, Integer applyId, Integer parkingSlotId, String slotNumber) {
		this.userId = userId;
		this.applyId = applyId;
		this.parkingSlotId = parkingSlotId;
		this.slotNumber = slotNumber;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getApplyId() {
		return applyId;
	}

	public void setApplyId(Integer applyId) {
		this.applyId = applyId;
	}

	public Integer getParkingSlotId() {
		return parkingSlotId;
	}

	public void setParkingSlotId(Integer parkingSlotId) {
		this.parkingSlotId = parkingSlotId;
	}

	public String getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(String slotNumber) {
		this.slotNumber = slotNumber;
	}

}
