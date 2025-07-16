package finalProj.dto.facilities.reservations;

import java.time.LocalDateTime;

public class ReservationRequest {

	private Integer facilityId; // 公設 ID
	private Integer unitId; // 預設可選擇傳，或從 account 推出
	private String username;    // 可以存 email 或登入帳號
	private Integer accountId; // 點數帳戶 ID（從這可得住戶單位）
	private LocalDateTime reservationStartTime;
	private LocalDateTime reservationEndTime;
	private Integer numberOfUsers;
	private Integer currentPoints; // 可用點數（雖然後端通常會自己查）
	private Integer deductAmount; // 扣點數量
	private Boolean isAdmin; // 是否管理員預約
	private String remark; // 備註
	
	
	public Integer getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(Integer facilityId) {
		this.facilityId = facilityId;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
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
	public Integer getNumberOfUsers() {
		return numberOfUsers;
	}
	public void setNumberOfUsers(Integer numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
	}
	public Integer getCurrentPoints() {
		return currentPoints;
	}
	public void setCurrentPoints(Integer currentPoints) {
		this.currentPoints = currentPoints;
	}
	public Integer getDeductAmount() {
		return deductAmount;
	}
	public void setDeductAmount(Integer deductAmount) {
		this.deductAmount = deductAmount;
	}
	public Boolean getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remarks) {
		this.remark = remarks;
	}
	


}
