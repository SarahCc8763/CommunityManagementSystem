package finalProj.domain.parking;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

	// 社區 Id
	@Column(name = "community_id")
	private Integer communityId;

	// 車位種類
	@Column(name = "type", nullable = false, length = 10)
	private String type;

	@Override
	public String toString() {
		return "ParkingType [id=" + id + ", communityId=" + communityId + ", type=" + type + "]";
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}