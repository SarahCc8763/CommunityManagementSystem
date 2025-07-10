package finalProj.domain.users;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import finalProj.domain.bulletin.Bulletin;
import finalProj.domain.bulletin.BulletinComment;
import finalProj.domain.bulletin.BulletinCommentLike;
import finalProj.domain.community.Community;
import finalProj.domain.feedback.Feedback;
import finalProj.domain.feedback.FeedbackReply;
import finalProj.domain.feedback.FeedbackStatusHistory;
import finalProj.domain.parking.LotteryApply;
import finalProj.domain.parking.LotteryEvents;
import finalProj.domain.parking.ParkingRentals;
import finalProj.domain.parking.ParkingSlot;
import finalProj.domain.poll.PollVote;
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
import jakarta.persistence.Transient;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({ "feedbacks", "bulletinComments", "bulletinCommentLikes", "pollVotes", "bulletins",
		"handledFeedbacks", "feedbackStatusHistories", "feedbackReplies", "RolesUsersList" })
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

	@Transient
	private List<Units> unit;

	@JsonBackReference("communityUser")
	@ManyToOne
	@JoinColumn(name = "community_id", referencedColumnName = "id")
	private Community community;

	// 這個 user 曾報修過的 tickets
	@OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("reporterUser")
	// @JsonIgnore
	private List<Ticket> reportedTickets;

	// 這個 user 被指派處理的 tickets
	@JsonManagedReference("assignerIdTicket")
	@OneToMany(mappedBy = "assignerId", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Ticket> assignedTickets;

	// --- 政宇的關聯 START ---

	@OneToMany(mappedBy = "user")
	@JsonManagedReference("feedback-user")
	private List<Feedback> feedbacks;

	@OneToMany(mappedBy = "user")
	@JsonManagedReference("bulletinComment-user")
	private List<BulletinComment> bulletinComments;

	@OneToMany(mappedBy = "user")
	@JsonManagedReference("bulletinCommentLike-user")
	private List<BulletinCommentLike> bulletinCommentLikes;

	@OneToMany(mappedBy = "user")
	@JsonManagedReference("pollVote-user")
	private List<PollVote> pollVotes;

	@OneToMany(mappedBy = "user")
	@JsonManagedReference("bulletin-user")
	private List<Bulletin> bulletins;

	@OneToMany(mappedBy = "handler")
	@JsonManagedReference("feedback-handler")
	private List<Feedback> handledFeedbacks;

	@OneToMany(mappedBy = "changedBy")
	@JsonManagedReference("feedbackStatusHistory-user")
	private List<FeedbackStatusHistory> feedbackStatusHistories;

	@OneToMany(mappedBy = "user")
	private List<FeedbackReply> feedbackReplies;

	// @JsonManagedReference("RoleUser")
	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RolesUsers> RolesUsersList;

	// --- 政宇的關聯 END ---

	// --- Julie的關聯 START ---
	// 一對多到車位
	@JsonManagedReference("users-parkingSlot")
	@OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ParkingSlot> parkingSlots;

	// 一對多到承租車位 (承租者)
	@JsonManagedReference("users-parkingRentals") // 與 users 對應
	@OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ParkingRentals> rentalsMade;

	// 一對多到承租車位 (審核者)
	@JsonManagedReference("users-approver") // 與 approver 對應
	@OneToMany(mappedBy = "approver", cascade = CascadeType.ALL)
	private List<ParkingRentals> rentalsApproved;

	// 一對多到車位抽籤
	@JsonManagedReference("users-lotteryEevents")
	@OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
	private List<LotteryEvents> lotteryEvents;

	// 一對多到車位抽籤申請
	@JsonManagedReference("users-lotteryApply")
	@OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
	private List<LotteryApply> lotteryApply;

	// --- Julie的關聯 END ---

	public Integer getUsersId() {
		return usersId;
	}

	public List<ParkingSlot> getParkingSlots() {
		return parkingSlots;
	}

	public void setParkingSlots(List<ParkingSlot> parkingSlots) {
		this.parkingSlots = parkingSlots;
	}

	public List<ParkingRentals> getRentalsMade() {
		return rentalsMade;
	}

	public void setRentalsMade(List<ParkingRentals> rentalsMade) {
		this.rentalsMade = rentalsMade;
	}

	public List<ParkingRentals> getRentalsApproved() {
		return rentalsApproved;
	}

	public void setRentalsApproved(List<ParkingRentals> rentalsApproved) {
		this.rentalsApproved = rentalsApproved;
	}

	public List<LotteryEvents> getLotteryEvents() {
		return lotteryEvents;
	}

	public void setLotteryEvents(List<LotteryEvents> lotteryEvents) {
		this.lotteryEvents = lotteryEvents;
	}

	public List<LotteryApply> getLotteryApply() {
		return lotteryApply;
	}

	public void setLotteryApply(List<LotteryApply> lotteryApply) {
		this.lotteryApply = lotteryApply;
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

	public List<Feedback> getFeedbacks() {
		return feedbacks;
	}

	public void setFeedbacks(List<Feedback> feedbacks) {
		this.feedbacks = feedbacks;
	}

	public List<BulletinComment> getBulletinComments() {
		return bulletinComments;
	}

	public void setBulletinComments(List<BulletinComment> bulletinComments) {
		this.bulletinComments = bulletinComments;
	}

	public List<BulletinCommentLike> getBulletinCommentLikes() {
		return bulletinCommentLikes;
	}

	public void setBulletinCommentLikes(List<BulletinCommentLike> bulletinCommentLikes) {
		this.bulletinCommentLikes = bulletinCommentLikes;
	}

	public List<PollVote> getPollVotes() {
		return pollVotes;
	}

	public void setPollVotes(List<PollVote> pollVotes) {
		this.pollVotes = pollVotes;
	}

	public List<Bulletin> getBulletins() {
		return bulletins;
	}

	public void setBulletins(List<Bulletin> bulletins) {
		this.bulletins = bulletins;
	}

	public List<Feedback> getHandledFeedbacks() {
		return handledFeedbacks;
	}

	public void setHandledFeedbacks(List<Feedback> handledFeedbacks) {
		this.handledFeedbacks = handledFeedbacks;
	}

	public List<FeedbackStatusHistory> getFeedbackStatusHistories() {
		return feedbackStatusHistories;
	}

	public void setFeedbackStatusHistories(List<FeedbackStatusHistory> feedbackStatusHistories) {
		this.feedbackStatusHistories = feedbackStatusHistories;
	}

	public List<FeedbackReply> getFeedbackReplies() {
		return feedbackReplies;
	}

	public void setFeedbackReplies(List<FeedbackReply> feedbackReplies) {
		this.feedbackReplies = feedbackReplies;
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

	@JsonIgnore
	public List<RolesUsers> getRolesUsersList() {
		return RolesUsersList;
	}

	public void setRolesUsersList(List<RolesUsers> rolesUsersList) {
		RolesUsersList = rolesUsersList;
	}

	public List<Units> getUnit() {
		return unitsUsersList == null
				? List.of()
				: unitsUsersList.stream()
						.map(UnitsUsers::getUnit)
						.filter(Objects::nonNull)
						.toList();
	}

	public void setUnit(List<Units> unit) {
		this.unit = unit;
	}
}
