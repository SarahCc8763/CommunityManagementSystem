package finalProj.domain.community;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import finalProj.domain.Bulletin;
import finalProj.domain.parking.ParkingRentals;
import finalProj.domain.parking.ParkingSlot;
import finalProj.domain.parking.ParkingType;
import finalProj.domain.users.Units;
import finalProj.domain.users.Users;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "community")
public class Community {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer communityId; // 社區流水號

	@Column(name = "name")
	private String name; // 社區名稱

	@Column(name = "address")
	private String address; // 地址

	@Column(name = "create_time", insertable = false, updatable = false)
	private java.util.Date createTime; // 創建時間

	@Column(name = "[function]") // SQL Server 保留字，需用中括號轉義
	private Long function; // 使用功能

	// ---------------------------------------------------------------------------------------

	// 一對多到戶
	@JsonManagedReference("community-units")
	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Units> units;

	// 一對多到使用者
	@JsonManagedReference("community-users")
	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Users> users;

	// 一對多到公告
	@JsonManagedReference("community-bulletin")
	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Bulletin> bulletin;

	// 一對多到車位種類
	@JsonManagedReference("community-parkingType")
	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ParkingType> parkingType;

	// 一對多到車位
	@JsonManagedReference("community-parkingSlot")
	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ParkingSlot> parkingSlot;

	// 一對多到承租紀錄
	@JsonManagedReference("community-parkingRentals")
	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ParkingRentals> parkingRentals;

	// ---------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "Community [communityId=" + communityId + ", name=" + name + ", address=" + address + ", createTime="
				+ createTime + ", function=" + function + "]";
	}

	public Integer getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Integer communityId) {
		this.communityId = communityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public Long getFunction() {
		return function;
	}

	public void setFunction(Long function) {
		this.function = function;
	}

	public List<Users> getUsers() {
		return users;
	}

	public void setUsers(List<Users> users) {
		this.users = users;
	}

	public List<ParkingType> getParkingType() {
		return parkingType;
	}

	public void setParkingType(List<ParkingType> parkingType) {
		this.parkingType = parkingType;
	}

	public List<ParkingSlot> getParkingSlot() {
		return parkingSlot;
	}

	public void setParkingSlot(List<ParkingSlot> parkingSlot) {
		this.parkingSlot = parkingSlot;
	}

	public List<ParkingRentals> getParkingRentals() {
		return parkingRentals;
	}

	public void setParkingRentals(List<ParkingRentals> parkingRentals) {
		this.parkingRentals = parkingRentals;
	}

	public List<Units> getUnits() {
		return units;
	}

	public void setUnits(List<Units> units) {
		this.units = units;
	}

	public List<Bulletin> getBulletin() {
		return bulletin;
	}

	public void setBulletin(List<Bulletin> bulletin) {
		this.bulletin = bulletin;
	}

}