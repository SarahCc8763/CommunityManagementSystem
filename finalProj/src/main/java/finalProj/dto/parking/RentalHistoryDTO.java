package finalProj.dto.parking;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class RentalHistoryDTO {
	private Integer id;
	private String licensePlate;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Taipei")
	private Date rentBuyStart;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Taipei")
	private Date rentEnd;
	private Boolean status;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	private Date createdAt;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	private Date updatedAt;
	private Boolean approved;

	// 關聯的部分
	private String slotNumber;
	private Integer slotId;
	private String location;
	private String parkingType;
	private Integer parkingTypeId;
	private String userName;
	private Integer usersId;
	private String approverName;
	private Integer approverId;

	public Integer getUsersId() {
		return usersId;
	}

	public void setUsersId(Integer usersId) {
		this.usersId = usersId;
	}

	public Integer getApproverId() {
		return approverId;
	}

	public void setApproverId(Integer approverId) {
		this.approverId = approverId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public Date getRentBuyStart() {
		return rentBuyStart;
	}

	public void setRentBuyStart(Date rentBuyStart) {
		this.rentBuyStart = rentBuyStart;
	}

	public Date getRentEnd() {
		return rentEnd;
	}

	public void setRentEnd(Date rentEnd) {
		this.rentEnd = rentEnd;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
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

	public String getParkingType() {
		return parkingType;
	}

	public void setParkingType(String parkingType) {
		this.parkingType = parkingType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

	public Integer getSlotId() {
		return slotId;
	}

	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
	}

	public Integer getParkingTypeId() {
		return parkingTypeId;
	}

	public void setParkingTypeId(Integer parkingTypeId) {
		this.parkingTypeId = parkingTypeId;
	}

}
