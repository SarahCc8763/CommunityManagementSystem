package finalProj.dto.parking;

import java.util.Date;

public class RentalHistoryDTO {
	private String licensePlate;
	private Date startDate;
	private Date endDate;
	private Boolean status;

	public RentalHistoryDTO(String licensePlate, Date startDate, Date endDate, Boolean status) {
		this.licensePlate = licensePlate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}
