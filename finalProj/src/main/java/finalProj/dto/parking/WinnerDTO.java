package finalProj.dto.parking;

public class WinnerDTO {
	private Integer id;
	private Integer userId;
	private String userName;
	private Integer applyId;
	private Integer parkingSlotId;
	private String slotNumber;
	private String parkingType;

	public WinnerDTO(Integer applyId, Integer userId, String userName, String slotNumber, String parkingType) {
		this.applyId = applyId;
		this.userId = userId;
		this.userName = userName;
		this.slotNumber = slotNumber;
		this.parkingType = parkingType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getParkingType() {
		return parkingType;
	}

	public void setParkingType(String parkingType) {
		this.parkingType = parkingType;
	}

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
