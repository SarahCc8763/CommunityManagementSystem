package finalProj.domain.users;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import finalProj.domain.community.Community;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles_users")
public class RolesUsers {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rolesusers_id")
	private Integer rolesusersId;

	@JsonBackReference("RoleUser")
	// @JsonIgnore
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "users_id", nullable = false)
	private Users user;

	@ManyToOne
	@JoinColumn(name = "role_id", referencedColumnName = "roles_id", nullable = false)
	private Roles role;

	// @EmbeddedId
	// private Integer rolesusersId;
	//
	//// @ManyToOne
	//// @MapsId("user")
	// @JoinColumn(name = "users_id")
	// private Users user;
	//
	//// @ManyToOne
	//// @MapsId("role")
	// @JoinColumn(name = "roles_id")
	// private Roles role;

	@ManyToOne
	@JoinColumn(name = "community_id", referencedColumnName = "id")
	private Community community;

	public Integer getRolesusersId() {
		return rolesusersId;
	}

	public void setRolesusersId(Integer rolesusersId) {
		this.rolesusersId = rolesusersId;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Roles getRole() {
		return role;
	}

	public void setRole(Roles role) {
		this.role = role;
	}

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	@Override
	public String toString() {
		return "RolesUsers [rolesusersId=" + rolesusersId + ", user=" + user + ", role=" + role + ", community="
				+ community + ", getRolesusersId()=" + getRolesusersId() + ", getUser()=" + getUser() + ", getRole()="
				+ getRole() + ", getCommunity()=" + getCommunity() + "]";
	}

}
