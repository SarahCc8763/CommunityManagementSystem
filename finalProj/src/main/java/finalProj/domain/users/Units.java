package finalProj.domain.users;

import java.math.BigDecimal;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import finalProj.domain.community.Community;
import finalProj.domain.facilities.FacilityReservationsBean;
import finalProj.domain.facilities.PointAccountsBean;
import finalProj.domain.facilities.PointTransactionsBean;
import finalProj.domain.packages.Packages;
import finalProj.domain.parking.TemporaryParkingApplication;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "Units", uniqueConstraints = { @UniqueConstraint(columnNames = { "unit", "floor" })
})
public class Units {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "units_id")
	private Integer unitsId;

	@Column(nullable = false, length = 10)
	private String unit;

	@Column(nullable = false, length = 10)
	private String floor;

	@Column(length = 10)
	private String building;

	@Column(nullable = false, precision = 6, scale = 2)
	private BigDecimal ping;

	@Column(columnDefinition = "INT DEFAULT 0")
	private Integer point = 0;

	@JsonManagedReference("unitPackage")
	@OneToMany(mappedBy = "unit", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Packages> packagesList;

	@OneToMany(mappedBy = "unit", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("unitsUsersList")
	private List<UnitsUsers> unitsUsersList;

	// @JsonBackReference("communityUnit")
	@ManyToOne
	@JoinColumn(name = "community_id", referencedColumnName = "id")
	private Community community;

	// --- Julie的關聯 START ---
	// 一對多到車位
	// @JsonManagedReference("units-temporaryParking")
	@JsonIgnore
	@OneToMany(mappedBy = "units", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TemporaryParkingApplication> temporaryParking;

	// --- Julie的關聯 END ---


	// --- javert的關聯 START ---

	//  對應 facility_reservations 單位預約（多對一）：
	@OneToMany(mappedBy = "unit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference("unit-reservations")
	private List<FacilityReservationsBean> reservations;

	//  對應 point_accounts 一對一帳戶關聯：
	@OneToOne(mappedBy = "unit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference("unit-account")
	private PointAccountsBean pointAccount;	

	//  對應 point_transactions 點數異動紀錄（多對一）：
	@OneToMany(mappedBy = "unit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference("unit-transactions")
	private List<PointTransactionsBean> pointTransactions;
	
	// --- javert的關聯 END ---


	public Integer getUnitsId() {
		return unitsId;
	}


	public void setUnitsId(Integer unitsId) {
		this.unitsId = unitsId;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public BigDecimal getPing() {
		return ping;
	}

	public void setPing(BigDecimal ping) {
		this.ping = ping;
	}

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public List<Packages> getPackagesList() {
		return packagesList;
	}

	public void setPackagesList(List<Packages> packagesList) {
		this.packagesList = packagesList;
	}

	public List<UnitsUsers> getUnitsUsersList() {
		return unitsUsersList;
	}

	public void setUnitsUsersList(List<UnitsUsers> unitsUsersList) {
		this.unitsUsersList = unitsUsersList;
	}

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	
	public List<FacilityReservationsBean> getReservations() {
		return reservations;
	}

	public void setReservations(List<FacilityReservationsBean> reservations) {
		this.reservations = reservations;
	}

	public PointAccountsBean getPointAccount() {
		return pointAccount;
	}

	public void setPointAccount(PointAccountsBean pointAccount) {
		this.pointAccount = pointAccount;
	}

	public List<PointTransactionsBean> getPointTransactions() {
		return pointTransactions;
	}

	public void setPointTransactions(List<PointTransactionsBean> pointTransactions) {
		this.pointTransactions = pointTransactions;
	}

	@Override
	public String toString() {
		return "Units [unitsId=" + unitsId + ", unit=" + unit + ", floor=" + floor + ", building=" + building
				+ ", ping=" + ping + ", point=" + point + ", packagesList=" + packagesList + ", unitsUsersList="
				+ unitsUsersList + ", community=" + community + "]";
	}

	public List<TemporaryParkingApplication> getTemporaryParking() {
		return temporaryParking;
	}

	public void setTemporaryParking(List<TemporaryParkingApplication> temporaryParking) {
		this.temporaryParking = temporaryParking;
	}

}
