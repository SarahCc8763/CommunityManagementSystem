//package finalProj.domain.ticket;
//
//import java.util.Date;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import jakarta.persistence.Temporal;
//import jakarta.persistence.TemporalType;
//
//@Entity
//@Table(name = "users")
//public class Users {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "users_id")
//	private Integer usersId;
//
//	@Column(name = "name", nullable = false)
//	private String name;
//
//	@Column(name = "gender")
//	private String gender;
//
//	@Column(name = "contact_info")
//	private String contactInfo;
//
//	@Column(name = "emergency_contact_name")
//	private String emergencyContactName;
//
//	@Column(name = "emergency_contact_phone")
//	private String emergencyContactPhone;
//
//	@Column(name = "emergency_contact_relation")
//	private String emergencyContactRelation;
//
//	@Column(name = "line_id")
//	private String lineId;
//
//	@Column(name = "state")
//	private String state;
//
//	@Column(name = "create_at", updatable = false)
//	@Temporal(TemporalType.TIMESTAMP)
//	private Date createAt;
//
//	@Column(name = "last_alter_at", nullable = false)
//	@Temporal(TemporalType.TIMESTAMP)
//	private Date lastAlterAt;
//
//	@Column(name = "photo")
//	private String photo; // 儲存圖片 URL
//
//	@Column(name = "email", unique = true)
//	private String email;
//
//	@Column(name = "community_id")
//	private Integer communityId;
//
//	@Override
//	public String toString() {
//		return "Users [usersId=" + usersId + ", name=" + name + ", gender=" + gender + ", contactInfo=" + contactInfo
//				+ ", emergencyContactName=" + emergencyContactName + ", emergencyContactPhone=" + emergencyContactPhone
//				+ ", emergencyContactRelation=" + emergencyContactRelation + ", lineId=" + lineId + ", state=" + state
//				+ ", createAt=" + createAt + ", lastAlterAt=" + lastAlterAt + ", photo=" + photo + ", email=" + email
//				+ ", communityId=" + communityId + "]";
//	}
//
//	public Integer getUsersId() {
//		return usersId;
//	}
//
//	public void setUsersId(Integer usersId) {
//		this.usersId = usersId;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public String getGender() {
//		return gender;
//	}
//
//	public void setGender(String gender) {
//		this.gender = gender;
//	}
//
//	public String getContactInfo() {
//		return contactInfo;
//	}
//
//	public void setContactInfo(String contactInfo) {
//		this.contactInfo = contactInfo;
//	}
//
//	public String getEmergencyContactName() {
//		return emergencyContactName;
//	}
//
//	public void setEmergencyContactName(String emergencyContactName) {
//		this.emergencyContactName = emergencyContactName;
//	}
//
//	public String getEmergencyContactPhone() {
//		return emergencyContactPhone;
//	}
//
//	public void setEmergencyContactPhone(String emergencyContactPhone) {
//		this.emergencyContactPhone = emergencyContactPhone;
//	}
//
//	public String getEmergencyContactRelation() {
//		return emergencyContactRelation;
//	}
//
//	public void setEmergencyContactRelation(String emergencyContactRelation) {
//		this.emergencyContactRelation = emergencyContactRelation;
//	}
//
//	public String getLineId() {
//		return lineId;
//	}
//
//	public void setLineId(String lineId) {
//		this.lineId = lineId;
//	}
//
//	public String getState() {
//		return state;
//	}
//
//	public void setState(String state) {
//		this.state = state;
//	}
//
//	public Date getCreateAt() {
//		return createAt;
//	}
//
//	public void setCreateAt(Date createAt) {
//		this.createAt = createAt;
//	}
//
//	public Date getLastAlterAt() {
//		return lastAlterAt;
//	}
//
//	public void setLastAlterAt(Date lastAlterAt) {
//		this.lastAlterAt = lastAlterAt;
//	}
//
//	public String getPhoto() {
//		return photo;
//	}
//
//	public void setPhoto(String photo) {
//		this.photo = photo;
//	}
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
//
//	public Integer getCommunityId() {
//		return communityId;
//	}
//
//	public void setCommunityId(Integer communityId) {
//		this.communityId = communityId;
//	}
//
//}
