package finalProj.dto.parking;

import java.util.Date;

public class TemporaryParkingDTO {

	private Integer id;
	private String visitorName;
	private String licensePlate;
	private String purpose;
	private Date requestTime;
	private Date startTime;
	private Date endTime;
	private Date createdAt;
	private Date updatedAt;

	private String slotNumber;
	private String location;
	private Integer parkingTypeId;

	private Integer unitsId;
 
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getVisitorName() {
		return visitorName;
	}
	public void setVisitorName(String visitorName) {
		this.visitorName = visitorName;
	}
	public String getLicensePlate() {
		return licensePlate;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public Date getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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

	public Integer getParkingTypeId() {
		return parkingTypeId;
	}
	public void setParkingTypeId(Integer parkingTypeId) {
		this.parkingTypeId = parkingTypeId;
	}
	public Integer getUnitsId() {
		return unitsId;
	}
	public void setUnitsId(Integer unitsId) {
		this.unitsId = unitsId;
	}


}
