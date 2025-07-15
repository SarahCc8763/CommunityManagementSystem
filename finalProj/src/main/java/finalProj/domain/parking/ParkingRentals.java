package finalProj.domain.parking;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import finalProj.domain.community.Community;
import finalProj.domain.users.Users;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

	// 多對一到社區
	@JsonBackReference("community-parkingRentals")
	@ManyToOne
	@JoinColumn(name = "community_id", referencedColumnName = "id")
	private Community community;

	// 多對一到車位資料
	@JsonBackReference("parkingSlot-parkingRentals")
	@ManyToOne
	@JoinColumn(name = "parking_slot_id", referencedColumnName = "id")
	private ParkingSlot parkingSlot;

	// 多對一到承租者
	@JsonBackReference("users-parkingRentals")
	@ManyToOne
	@JoinColumn(name = "users_id", referencedColumnName = "users_id")
	private Users users;

	// 多對一到審核人
	@JsonBackReference("users-approver")
	@ManyToOne
	@JoinColumn(name = "approved_id", referencedColumnName = "users_id")
	private Users approver;

	// 承租起始日
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Taipei")
	@Column(name = "rent_buy_start", nullable = false)
	private Date rentBuyStart;

	// 承租截止日
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Taipei")
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
	private Boolean approved;

	// 最後更新時間
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	@Column(name = "updated_at")
	private Date updatedAt;

	// 創建時間
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	@Column(name = "created_at")
	private Date createdAt;

	// ---------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "ParkingRentals [id=" + id + ", community=" + community + ", parkingSlot=" + parkingSlot + ", users="
				+ users + ", approver=" + approver + ", rentBuyStart=" + rentBuyStart + ", rentEnd=" + rentEnd
				+ ", licensePlate=" + licensePlate + ", status=" + status + ", approved=" + approved + ", updatedAt="
				+ updatedAt + ", createdAt=" + createdAt + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	public ParkingSlot getParkingSlot() {
		return parkingSlot;
	}

	public void setParkingSlot(ParkingSlot parkingSlot) {
		this.parkingSlot = parkingSlot;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public Users getApprover() {
		return approver;
	}

	public void setApprover(Users approver) {
		this.approver = approver;
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
