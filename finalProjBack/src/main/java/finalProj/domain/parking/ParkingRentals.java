package finalProj.domain.parking;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// 承租紀錄表
@Entity
@Table(name = "parking_rentals")
public class ParkingRentals {

	// 流水號
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	// 社區 Id
	@Column(name = "community_id")
	private Integer communityId;

	// 承租者 Id
	@Column(name = "users_id", nullable = false)
	private Integer usersId;

	// 車位 Id
	@Column(name = "parking_slot_id", nullable = false)
	private Integer parkingSlotId;

	// 承租起始日
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	@Column(name = "rent_buy_start", nullable = false)
	private Date rentBuyStart;

	// 承租截止日
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	@Column(name = "rent_end", nullable = false)
	private Date rentEnd;

	// 登記車牌
	@Column(name = "license_plate", nullable = false, length = 10)
	private String licensePlate;

	// 繳費狀態
	@Column(name = "status", nullable = false)
	private Boolean status;

	// 是否已審核
	@Column(name = "approved", nullable = false)
	private Boolean approved = false;

	// 審核人 Id（管理員）
	@Column(name = "approved_id")
	private Integer approvedId;

	// 最後更新時間
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	@Column(name = "updated_at")
	private Date updatedAt;

	// 創建時間
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	@Column(name = "created_at")
	private Date createdAt;

	@Override
	public String toString() {
		return "ParkingRentals [id=" + id + ", communityId=" + communityId + ", usersId=" + usersId + ", parkingSlotId="
				+ parkingSlotId + ", rentBuyStart=" + rentBuyStart + ", rentEnd=" + rentEnd + ", licensePlate="
				+ licensePlate + ", status=" + status + ", approved=" + approved + ", approvedId=" + approvedId
				+ ", updatedAt=" + updatedAt + ", createdAt=" + createdAt + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Integer communityId) {
		this.communityId = communityId;
	}

	public Integer getUsersId() {
		return usersId;
	}

	public void setUsersId(Integer usersId) {
		this.usersId = usersId;
	}

	public Integer getParkingSlotId() {
		return parkingSlotId;
	}

	public void setParkingSlotId(Integer parkingSlotId) {
		this.parkingSlotId = parkingSlotId;
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

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	public Integer getApprovedId() {
		return approvedId;
	}

	public void setApprovedId(Integer approvedId) {
		this.approvedId = approvedId;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
