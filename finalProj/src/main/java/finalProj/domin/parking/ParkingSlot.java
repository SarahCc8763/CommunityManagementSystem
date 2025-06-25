package finalProj.domain.parking;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// 車位資料表
@Entity
@Table(name = "parking_slot")
public class ParkingSlot {

	// 流水號
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	// 社區 Id
	@Column(name = "community_id")
	private Integer communityId;

	// 車位代碼
	@Column(name = "slot_number", length = 10, nullable = false)
	private String slotNumber;

	// 位置
	@Column(name = "location", length = 20)
	private String location;

	// 種類 Id
	@Column(name = "parking_type_id", nullable = false)
	private Integer parkingTypeId;

	// 擁有人 Id
	@Column(name = "users_id", nullable = false)
	private Integer usersId;

	// 登記車牌
	@Column(name = "license_plate", length = 10)
	private String licensePlate;

	// 是否可承租
	@Column(name = "is_rentable", nullable = false)
	private Boolean isRentable;

	@Override
	public String toString() {
		return "ParkingSlot [id=" + id + ", communityId=" + communityId + ", slotNumber=" + slotNumber + ", location="
				+ location + ", parkingTypeId=" + parkingTypeId + ", usersId=" + usersId + ", licensePlate="
				+ licensePlate + ", isRentable=" + isRentable + "]";
	}

	// Getters and Setters
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

	public Integer getUsersId() {
		return usersId;
	}

	public void setUsersId(Integer userId) {
		this.usersId = userId;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public Boolean getIsRentable() {
		return isRentable;
	}

	public void setIsRentable(Boolean isRentable) {
		this.isRentable = isRentable;
	}
}
