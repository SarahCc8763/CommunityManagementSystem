package finalProj.dto.parking;

public class ParkingSlotDTO {
	private Integer id;
	private String slotNumber;
	private String location;
	private Boolean isRentable;
	private String licensePlate;
	private String parkingType;
	private Integer parkingTypeId;
	private String parkingTypeName;
	private Integer usersId;
	private String userName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(String slotNumber) {
		this.slotNumber = slotNumber;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Boolean getIsRentable() {
		return isRentable;
	}

	public void setIsRentable(Boolean isRentable) {
		this.isRentable = isRentable;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public String getParkingType() {
		return parkingType;
	}

	public void setParkingType(String parkingType) {
		this.parkingType = parkingType;
	}

	public Integer getParkingTypeId() {
		return parkingTypeId;
	}

	public void setParkingTypeId(Integer parkingTypeId) {
		this.parkingTypeId = parkingTypeId;
	}

	public String getParkingTypeName() {
		return parkingTypeName;
	}

	public void setParkingTypeName(String parkingTypeName) {
		this.parkingTypeName = parkingTypeName;
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

}
