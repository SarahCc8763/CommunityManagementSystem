package finalProj.domain.facilities;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import finalProj.domain.community.Community;
import finalProj.domain.users.Units;
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
import jakarta.persistence.Table;

@Entity
@Table(name = "facility_reservations")
public class FacilityReservationsBean {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reservation_id")
	private Integer reservationId;
	
	@ManyToOne
	@JoinColumn(name = "community_id", referencedColumnName = "id")
	@JsonBackReference("faciliy-reservation")
	private Community community;
	
	@ManyToOne
	@JoinColumn(name = "unit_id", referencedColumnName = "units_id")
	@JsonBackReference("unit-reservations")
	private Units unit;

	@ManyToOne	
	@JoinColumn(name = "facility_id", referencedColumnName = "facility_id")
	@JsonBackReference("facility-reservations")
	private FacilitiesBean facility;

	@Column(name = "number_of_users")
	private Integer numberOfUsers;

	@Column(name = "reservation_start_time", nullable = false)
	private LocalDateTime reservationStartTime;

	@Column(name = "reservation_end_time", nullable = false)
	private LocalDateTime reservationEndTime;

	@Column(name = "is_admin", nullable = false)
	private Boolean isAdmin = false;

	@Column(name = "required_points")
	private Integer requiredPoints;

	@Column(name = "actual_used_points")
	private Integer actualUsedPoints;
	
	@Column(name = "point_expire_At")
	private LocalDateTime pointExpireAt;

	@Column(name = "remark")
	private String remark;

	@Column(name = "reservation_status")
	private String reservationStatus = "APPROVED";

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "canceled_at")
	private LocalDateTime canceledAt;

	@Column(name = "cancel_reason")
	private String cancelReason;
	
	@OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference("point-transactions")
	private List<PointTransactionsBean> pointTransactions;
	
	// ---------- Getters & Setters ----------

	public Integer getReservationId() {
		return reservationId;
	}

	public void setReservationId(Integer reservationId) {
		this.reservationId = reservationId;
	}

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	public Units getUnit() {
		return unit;
	}

	public void setUnit(Units unit) {
		this.unit = unit;
	}

	public FacilitiesBean getFacility() {
		return facility;
	}

	public void setFacility(FacilitiesBean facility) {
		this.facility = facility;
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

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Integer getRequiredPoints() {
		return requiredPoints;
	}

	public void setRequiredPoints(Integer requiredPoints) {
		this.requiredPoints = requiredPoints;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public LocalDateTime getCanceledAt() {
		return canceledAt;
	}

	public void setCanceledAt(LocalDateTime canceledAt) {
		this.canceledAt = canceledAt;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public List<PointTransactionsBean> getPointTransactions() {
		return pointTransactions;
	}

	public void setPointTransactions(List<PointTransactionsBean> pointTransactions) {
		this.pointTransactions = pointTransactions;
	}
	


	// ---------- toString ----------

	@Override
	public String toString() {
	    return "FacilityReservationsBean{" +
	            "reservationId=" + reservationId +
	            ", communityId=" + (community != null ? community.getCommunityId() : null) +
	            ", unitId=" + (unit != null ? unit.getUnitsId() : null) +
	            ", facilityId=" + (facility != null ? facility.getFacilityId() : null) +
	            ", numberOfUsers=" + numberOfUsers +
	            ", reservationStartTime=" + reservationStartTime +
	            ", reservationEndTime=" + reservationEndTime +
	            ", isAdmin=" + isAdmin +
	            ", requiredPoints=" + requiredPoints +
	            ", actualUsedPoints=" + actualUsedPoints +
	            ", pointExpireAt=" + pointExpireAt +
	            ", remark='" + remark + '\'' +
	            ", reservationStatus='" + reservationStatus + '\'' +
	            ", createdAt=" + createdAt +
	            ", updatedAt=" + updatedAt +
	            ", canceledAt=" + canceledAt +
	            ", cancelReason='" + cancelReason + '\'' +
	            '}';
	}
}
