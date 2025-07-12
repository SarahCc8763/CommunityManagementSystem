package finalProj.domain.facilities;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import finalProj.domain.community.Community;
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
@Table(name = "facilities")
public class FacilitiesBean {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "facility_id")
	private Integer facilityId;
		
	@ManyToOne
	@JoinColumn(name = "community_id", referencedColumnName = "id")
	@JsonBackReference("facilities")
	private Community community;	

	@Column(name = "facility_name", nullable = false)
	private String facilityName;

	@Column(name = "max_users")
	private Integer maxUsers;

	@Column(name = "facility_description")
	private String facilityDescription;

	@Column(name = "facility_location", nullable = false)
	private String facilityLocation;

	@Column(name = "open_time", nullable = false)
	private LocalTime openTime;

	@Column(name = "close_time", nullable = false)
	private LocalTime closeTime;

	@Column(name = "reservable_duration")
	private Integer reservableDuration;

	@Column(name = "fee", nullable = false)
	private Integer fee = 0;

	@Column(name = "facility_status", nullable = false)
	private String facilityStatus = "active";

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference("facility-images")
	private List<FacilityImagesBean> images;

	@OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference("facility-reservations")
	private List<FacilityReservationsBean> reservations;

	// ---------- Getter & Setter ----------

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}
	
	public Integer getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(Integer facilityId) {
		this.facilityId = facilityId;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public Integer getMaxUsers() {
		return maxUsers;
	}

	public void setMaxUsers(Integer maxUsers) {
		this.maxUsers = maxUsers;
	}

	public String getFacilityDescription() {
		return facilityDescription;
	}

	public void setFacilityDescription(String facilityDescription) {
		this.facilityDescription = facilityDescription;
	}

	public String getFacilityLocation() {
		return facilityLocation;
	}

	public void setFacilityLocation(String facilityLocation) {
		this.facilityLocation = facilityLocation;
	}

	public LocalTime getOpenTime() {
		return openTime;
	}

	public void setOpenTime(LocalTime openTime) {
		this.openTime = openTime;
	}

	public LocalTime getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(LocalTime closeTime) {
		this.closeTime = closeTime;
	}

	public Integer getReservableDuration() {
		return reservableDuration;
	}

	public void setReservableDuration(Integer reservableDuration) {
		this.reservableDuration = reservableDuration;
	}

	public Integer getFee() {
		return fee;
	}

	public void setFee(Integer fee) {
		this.fee = fee;
	}

	public String getFacilityStatus() {
		return facilityStatus;
	}

	public void setFacilityStatus(String facilityStatus) {
		this.facilityStatus = facilityStatus;
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

	public List<FacilityImagesBean> getImages() {
		return images;
	}

	public void setImages(List<FacilityImagesBean> images) {
		this.images = images;
	}

	public List<FacilityReservationsBean> getReservations() {
		return reservations;
	}

	public void setReservations(List<FacilityReservationsBean> reservations) {
		this.reservations = reservations;
	}

	// ---------- toString ----------

	@Override
	public String toString() {
	    return "FacilitiesBean{" +
	            "facilityId=" + facilityId +
	            ", communityId=" + (community != null ? community.getCommunityId() : null) +
	            ", facilityName='" + facilityName + '\'' +
	            ", maxUsers=" + maxUsers +
	            ", facilityDescription='" + facilityDescription + '\'' +
	            ", facilityLocation='" + facilityLocation + '\'' +
	            ", openTime=" + openTime +
	            ", closeTime=" + closeTime +
	            ", reservableDuration=" + reservableDuration +
	            ", fee=" + fee +
	            ", facilityStatus='" + facilityStatus + '\'' +
	            ", createdAt=" + createdAt +
	            ", updatedAt=" + updatedAt +
	            '}';
	}
}
