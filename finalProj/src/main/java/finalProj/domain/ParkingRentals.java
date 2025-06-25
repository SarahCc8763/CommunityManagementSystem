package finalProj.domain;

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

	@Override
	public String toString() {
		return "ParkingRentals [id=" + id + ", usersId=" + usersId + ", parkingSlotId=" + parkingSlotId
				+ ", rentBuyStart=" + rentBuyStart + ", rentEnd=" + rentEnd + ", licensePlate=" + licensePlate
				+ ", status=" + status + "]";
	}

	// Getters and Setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
}
