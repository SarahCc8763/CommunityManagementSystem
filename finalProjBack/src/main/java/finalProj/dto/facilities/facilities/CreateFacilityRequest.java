package finalProj.dto.facilities.facilities;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CreateFacilityRequest {

	private Integer communityId;
	
	private String facilityName;

	private Integer maxUsers;

	private String facilityDescription;

	private String facilityLocation;

	@JsonFormat(pattern = "HH:mm")
	private LocalTime openTime;

	@JsonFormat(pattern = "HH:mm")
	private LocalTime closeTime;

	private Integer reservableDuration;

	private Integer fee;

	public Integer getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Integer communityId) {
		this.communityId = communityId;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public Integer getMaxUsers() {
		return maxUsers;
	}

	public void setMaxUsers(Integer maxUsers) {
		this.maxUsers = maxUsers;
	}

	public String getFacilityDescription() {
		return facilityDescription;
	}

	public void setFacilityDescription(String facilityDescription) {
		this.facilityDescription = facilityDescription;
	}

	public String getFacilityLocation() {
		return facilityLocation;
	}

	public void setFacilityLocation(String facilityLocation) {
		this.facilityLocation = facilityLocation;
	}

	public LocalTime getOpenTime() {
		return openTime;
	}

	public void setOpenTime(LocalTime openTime) {
		this.openTime = openTime;
	}

	public LocalTime getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(LocalTime closeTime) {
		this.closeTime = closeTime;
	}

	public Integer getReservableDuration() {
		return reservableDuration;
	}

	public void setReservableDuration(Integer reservableDuration) {
		this.reservableDuration = reservableDuration;
	}

	public Integer getFee() {
		return fee;
	}

	public void setFee(Integer fee) {
		this.fee = fee;
	}
}
