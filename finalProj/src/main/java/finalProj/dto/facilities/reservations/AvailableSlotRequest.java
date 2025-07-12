package finalProj.dto.facilities.reservations;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;

public class AvailableSlotRequest {

	//請求可用預定slot的輸入request
	
	@NotNull(message = "facilityId 不可為空")
	private Integer facilityId;

	@NotNull(message = "起始日期不可為空")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate fromDate;

	@NotNull(message = "結束日期不可為空")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate toDate;

	// getters and setters

	public Integer getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(Integer facilityId) {
		this.facilityId = facilityId;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

}
