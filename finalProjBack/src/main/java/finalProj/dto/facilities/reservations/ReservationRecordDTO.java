package finalProj.dto.facilities.reservations;

import java.time.LocalDateTime;

public class ReservationRecordDTO {
	private Integer reservationId;	
	private String facilityName;
    private Integer numberOfUsers;
    private LocalDateTime reservationStartTime;
    private LocalDateTime reservationEndTime;
    private Integer actualUsedPoints;
    private LocalDateTime pointExpireAt;
    private String remark;
    private String reservationStatus;        // 原始狀態值，如 APPROVED    
    private String cancelReason;
    private LocalDateTime finalActionTime;   // = max(createdAt, canceledAt)
	
    public Integer getReservationId() {
		return reservationId;
	}
	public void setReservationId(Integer reservationId) {
		this.reservationId = reservationId;
	}
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	public Integer getNumberOfUsers() {
		return numberOfUsers;
	}
	public void setNumberOfUsers(Integer numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
	}
	public LocalDateTime getReservationStartTime() {
		return reservationStartTime;
	}
	public void setReservationStartTime(LocalDateTime reservationStartTime) {
		this.reservationStartTime = reservationStartTime;
	}
	public LocalDateTime getReservationEndTime() {
		return reservationEndTime;
	}
	public void setReservationEndTime(LocalDateTime reservationEndTime) {
		this.reservationEndTime = reservationEndTime;
	}
	public Integer getActualUsedPoints() {
		return actualUsedPoints;
	}
	public void setActualUsedPoints(Integer actualUsedPoints) {
		this.actualUsedPoints = actualUsedPoints;
	}
	public LocalDateTime getPointExpireAt() {
		return pointExpireAt;
	}
	public void setPointExpireAt(LocalDateTime pointExpireAt) {
		this.pointExpireAt = pointExpireAt;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getReservationStatus() {
		return reservationStatus;
	}
	public void setReservationStatus(String reservationStatus) {
		this.reservationStatus = reservationStatus;
	}
	public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	public LocalDateTime getFinalActionTime() {
		return finalActionTime;
	}
	public void setFinalActionTime(LocalDateTime finalActionTime) {
		this.finalActionTime = finalActionTime;
	}
}
