package finalProj.domain.parking;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import finalProj.domain.community.Community;
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

// 車位種類表
@Entity
@Table(name = "parking_type")
public class ParkingType {

	// 流水號
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	// 多對一到社區
	@JsonBackReference("community-parkingType")
	@ManyToOne
	@JoinColumn(name = "community_id", referencedColumnName = "id")
	private Community community;

	// 車位種類
	@Column(name = "type", nullable = false, length = 10)
	private String type;

	// ---------------------------------------------------------------------------------------

	@JsonManagedReference("parkingType-parkingSlot")
	@OneToMany(mappedBy = "parkingType", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ParkingSlot> parkingSlots;

	// ---------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "ParkingType [id=" + id + ", community=" + community + ", type=" + type + ", parkingSlots=" + parkingSlots
				+ "]";
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<ParkingSlot> getParkingSlots() {
		return parkingSlots;
	}

	public void setParkingSlots(List<ParkingSlot> parkingSlots) {
		this.parkingSlots = parkingSlots;
	}

}