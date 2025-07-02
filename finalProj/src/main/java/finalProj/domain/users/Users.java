package finalProj.domain.users;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import finalProj.domain.community.Community;
import finalProj.domain.ticket.Ticket;
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

@Entity
@Table(name = "users")
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "users_id")
	private Integer usersId;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(length = 10)
	private String gender;

	@Column(name = "contact_info", length = 255)
	private String contactInfo;

	@Column(name = "emergency_contact_name", length = 100)
	private String emergencyContactName;

	@Column(name = "emergency_contact_phone", length = 50)
	private String emergencyContactPhone;

	@Column(name = "emergency_contact_relation", length = 50)
	private String emergencyContactRelation;

	@Column(name = "line_id", length = 100)
	private String lineId;

	@Column(length = 50)
	private String state;

	@Column(name = "create_at")
	private LocalDateTime createAt;

	@Column(name = "last_alter_at", nullable = false)
	private LocalDateTime lastAlterAt;

	@Column(name = "photo", length = 500) // url
	private String photo;

	@Column(unique = true, length = 100)
	private String email;

	@Column(name = "password", nullable = false, length = 100)
	@JsonIgnore // 防止 password 被序列化回傳
	private String password;

	@Column(name = "states", columnDefinition = "INT DEFAULT 0")
	private Integer states = 0;

	@Column(name = "login_fail_times", columnDefinition = "INT DEFAULT 0")
	private Integer loginFailTimes = 0;

	@Column(name = "last_failed_login")
	private LocalDateTime lastFailedLogin;

	@Column(name = "account_locked_until")
	private LocalDateTime accountLockedUntil;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	// @JsonManagedReference("unitsUsers")
	@JsonIgnore
	private List<UnitsUsers> unitsUsersList;

	@JsonBackReference("communityUser")

	@ManyToOne
	@JoinColumn(name = "community_id", referencedColumnName = "id")
	private Community community;


	// 這個 user 曾報修過的 tickets
	@JsonManagedReference("reporterIdTicket")
	@OneToMany(mappedBy = "reporterId", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Ticket> reportedTickets;

	// 這個 user 被指派處理的 tickets
	@JsonManagedReference("assignerIdTicket")
	@OneToMany(mappedBy = "assignerId", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Ticket> assignedTickets;

	public Integer getUsersId() {
		return usersId;
	}

	public void setUsersId(Integer usersId) {
		this.usersId = usersId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

	public String getEmergencyContactName() {
		return emergencyContactName;
	}

	public void setEmergencyContactName(String emergencyContactName) {
		this.emergencyContactName = emergencyContactName;
	}

	public String getEmergencyContactPhone() {
		return emergencyContactPhone;
	}

	public void setEmergencyContactPhone(String emergencyContactPhone) {
		this.emergencyContactPhone = emergencyContactPhone;
	}

	public String getEmergencyContactRelation() {
		return emergencyContactRelation;
	}

	public void setEmergencyContactRelation(String emergencyContactRelation) {
		this.emergencyContactRelation = emergencyContactRelation;
	}

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public LocalDateTime getCreateAt() {
		return createAt;
	}

	public void setCreateAt(LocalDateTime createAt) {
		this.createAt = createAt;
	}

	public LocalDateTime getLastAlterAt() {
		return lastAlterAt;
	}

	public void setLastAlterAt(LocalDateTime lastAlterAt) {
		this.lastAlterAt = lastAlterAt;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getStates() {
		return states;
	}

	public void setStates(Integer states) {
		this.states = states;
	}

	public Integer getLoginFailTimes() {
		return loginFailTimes;
	}

	public void setLoginFailTimes(Integer loginFailTimes) {
		this.loginFailTimes = loginFailTimes;
	}

	public LocalDateTime getLastFailedLogin() {
		return lastFailedLogin;
	}

	public void setLastFailedLogin(LocalDateTime lastFailedLogin) {
		this.lastFailedLogin = lastFailedLogin;
	}

	public LocalDateTime getAccountLockedUntil() {
		return accountLockedUntil;
	}

	public void setAccountLockedUntil(LocalDateTime accountLockedUntil) {
		this.accountLockedUntil = accountLockedUntil;
	}

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	<<<<<<<HEAD=======

	public List<Ticket> getReportedTickets() {
		return reportedTickets;
	}

	public void setReportedTickets(List<Ticket> reportedTickets) {
		this.reportedTickets = reportedTickets;
	}

	public List<Ticket> getAssignedTickets() {
		return assignedTickets;
	}

	public void setAssignedTickets(List<Ticket> assignedTickets) {
		this.assignedTickets = assignedTickets;
	}

	public List<UnitsUsers> getUnitsUsersList() {
		return unitsUsersList;
	}

	public void setUnitsUsersList(List<UnitsUsers> unitsUsersList) {
        this.unitsUsersList = unitsUsersList;
    }

	

	@Override
	public String toString() {
		return "Users [usersId=" + usersId + ", name=" + name + ", gender=" + gender + ", contactInfo=" + contactInfo
				+ ", emergencyContactName=" + emergencyContactName + ", emergencyContactPhone=" + emergencyContactPhone
				+ ", emergencyContactRelation=" + emergencyContactRelation + ", lineId=" + lineId + ", state=" + state
				+ ", createAt=" + createAt + ", lastAlterAt=" + lastAlterAt + ", photo=" + photo + ", email=" + email
				+ ", password=" + password + ", states=" + states + ", loginFailTimes=" + loginFailTimes
				+ ", lastFailedLogin=" + lastFailedLogin + ", accountLockedUntil=" + accountLockedUntil
				+ ", unitsUsersList=" + unitsUsersList + ", community=" + community + ", reportedTickets="
				+ reportedTickets + ", assignedTickets=" + assignedTickets + "]";
	}

}
