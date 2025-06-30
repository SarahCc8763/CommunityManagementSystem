package finalProj.domain.parking;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import finalProj.domain.community.Community;
import finalProj.domain.users.Users;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

// 車位資料表
@Entity
@Table(name = "parking_slot", uniqueConstraints = @UniqueConstraint(columnNames = { "community_id", "slot_number" }))
public class ParkingSlot {

	// 流水號
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	// 多對一到社區
	@JsonBackReference("community-parkingSlot")
	@ManyToOne
	@JoinColumn(name = "community_id", referencedColumnName = "id")
	private Community community;

	// 多對一到車位種類
	@JsonBackReference("parkingType-parkingSlot")
	@ManyToOne
	@JoinColumn(name = "parking_type_id", referencedColumnName = "id")
	private ParkingType parkingType;

	// 多對一到擁有人
	@JsonBackReference("users-parkingSlot")
	@ManyToOne
	@JoinColumn(name = "users_id", referencedColumnName = "users_id")
	private Users users;

	// 車位代碼
	@Column(name = "slot_number", length = 10, nullable = false)
	private String slotNumber;

	// 位置
	@Column(name = "location", length = 20)
	private String location;

	// 登記車牌
	@Column(name = "license_plate", length = 10)
	private String licensePlate;

	// 是否可承租
	@Column(name = "is_rentable", nullable = false)
	private Boolean isRentable;

	// ---------------------------------------------------------------------------------------

	// 一對多到承租紀錄
	@JsonManagedReference("parkingSlot-parkingRentals")
	@OneToMany(mappedBy = "parkingSlot", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ParkingRentals> parkingRentals;

	// ---------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "ParkingSlot [id=" + id + ", community=" + community + ", parkingType=" + parkingType + ", users="
				+ users + ", slotNumber=" + slotNumber + ", location=" + location + ", licensePlate=" + licensePlate
				+ ", isRentable=" + isRentable + ", parkingRentals=" + parkingRentals + "]";
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

	public ParkingType getParkingType() {
		return parkingType;
	}

	public void setParkingType(ParkingType parkingType) {
		this.parkingType = parkingType;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
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

	public List<ParkingRentals> getParkingRentals() {
		return parkingRentals;
	}

	public void setParkingRentals(List<ParkingRentals> parkingRentals) {
		this.parkingRentals = parkingRentals;
	}

}
