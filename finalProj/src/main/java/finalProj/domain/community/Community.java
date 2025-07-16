package finalProj.domain.community;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import finalProj.domain.bulletin.Bulletin;
import finalProj.domain.bulletin.BulletinCategory;
import finalProj.domain.facilities.FacilitiesBean;
import finalProj.domain.facilities.FacilityReservationsBean;
import finalProj.domain.facilities.PointAccountsBean;
import finalProj.domain.facilities.PointTransactionsBean;
import finalProj.domain.faq.Faq;
import finalProj.domain.faq.FaqCategory;
import finalProj.domain.faq.FaqKeyword;
import finalProj.domain.feedback.Feedback;
import finalProj.domain.feedback.FeedbackCategory;
import finalProj.domain.packages.Packages;
import finalProj.domain.parking.ParkingRentals;
import finalProj.domain.parking.ParkingSlot;
import finalProj.domain.parking.ParkingType;
import finalProj.domain.ticket.Ticket;
import finalProj.domain.users.Units;
import finalProj.domain.users.Users;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "community")
@JsonIgnoreProperties({ "bulletins", "bulletinCategories", "faqCategories", "feedbackCategories", "feedbacks", "faqs",
		"faqKeywords" })
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

	// @JsonManagedReference("communityTicket")
	@JsonIgnore
	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Ticket> tickets;

	// @JsonManagedReference("communityUser")
	@JsonIgnore
	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Users> users;

	// @JsonManagedReference("communityUnit")
	@JsonIgnore
	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Units> units;

	@JsonManagedReference("communityPackage")
	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Packages> packagesList;

	// --- 政宇的關聯 START ---
	// 社區-公告
	@OneToMany(mappedBy = "community")
	@JsonManagedReference("community-bulletin")
	@JsonIgnore
	private List<Bulletin> bulletins;
	// 社區-公告分類
	@OneToMany(mappedBy = "community")
	@JsonManagedReference("community-bulletin-category")
	private List<BulletinCategory> bulletinCategories;
	// 社區-FAQ分類
	@OneToMany(mappedBy = "community")
	@JsonManagedReference("community-faq-category")
	private List<FaqCategory> faqCategories;
	// 社區-意見分類
	@OneToMany(mappedBy = "community")
	@JsonManagedReference("community-feedback-category")
	private List<FeedbackCategory> feedbackCategories;
	// 社區-意見
	@OneToMany(mappedBy = "community")
	@JsonManagedReference("community-feedback")
	private List<Feedback> feedbacks;
	// 社區-FAQ
	@OneToMany(mappedBy = "community")
	@JsonManagedReference("community-faq")
	private List<Faq> faqs;
	// 社區-FAQ關鍵字
	@OneToMany(mappedBy = "community")
	@JsonManagedReference("community-faq-keyword")
	private List<FaqKeyword> faqKeywords;

	// --- 政宇的關聯 END ---

	// --- Julie的關聯 START ---
	// 一對多到車位種類
	// @JsonManagedReference("community-parkingType")
	@JsonIgnore
	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ParkingType> parkingType;

	// 一對多到車位
	// @JsonManagedReference("community-parkingSlot")
	@JsonIgnore
	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ParkingSlot> parkingSlot;

	// 一對多到承租紀錄
	// @JsonManagedReference("community-parkingRentals")
	@JsonIgnore
	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ParkingRentals> parkingRentals;

	// --- Julie的關聯 END ---

	// --- javert關聯 start ---
	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference("facilities")
	private List<FacilitiesBean> facilities;

	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference("faciliy-reservation")
	private List<FacilityReservationsBean> reservations;

	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference("point-accounts")
	private List<PointAccountsBean> pointAccounts;

	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference("point-transactionsC")
	private List<PointTransactionsBean> pointTransactions;

	// --- javert關聯 end ---

	@Override
	public String toString() {
		return "Community [communityId=" + communityId + ", tickets=" + tickets + ", name=" + name + ", address="
				+ address + ", createTime=" + createTime + ", function=" + function + "]";
	}

	public List<Units> getUnits() {
		return units;
	}

	public void setUnits(List<Units> units) {
		this.units = units;
	}

	public List<Packages> getPackagesList() {
		return packagesList;
	}

	public void setPackagesList(List<Packages> packagesList) {
		this.packagesList = packagesList;
	}

	public List<Bulletin> getBulletins() {
		return bulletins;
	}

	public void setBulletins(List<Bulletin> bulletins) {
		this.bulletins = bulletins;
	}

	public List<BulletinCategory> getBulletinCategories() {
		return bulletinCategories;
	}

	public void setBulletinCategories(List<BulletinCategory> bulletinCategories) {
		this.bulletinCategories = bulletinCategories;
	}

	public List<FaqCategory> getFaqCategories() {
		return faqCategories;
	}

	public void setFaqCategories(List<FaqCategory> faqCategories) {
		this.faqCategories = faqCategories;
	}

	public List<FeedbackCategory> getFeedbackCategories() {
		return feedbackCategories;
	}

	public void setFeedbackCategories(List<FeedbackCategory> feedbackCategories) {
		this.feedbackCategories = feedbackCategories;
	}

	public List<Feedback> getFeedbacks() {
		return feedbacks;
	}

	public void setFeedbacks(List<Feedback> feedbacks) {
		this.feedbacks = feedbacks;
	}

	public List<Faq> getFaqs() {
		return faqs;
	}

	public void setFaqs(List<Faq> faqs) {
		this.faqs = faqs;
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

	public Integer getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Integer communityId) {
		this.communityId = communityId;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
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

	public List<Users> getUsers() {
		return users;
	}

	public void setUsers(List<Users> users) {
		this.users = users;
	}

	public Long getFunction() {
		return function;
	}

	public void setFunction(Long function) {
		this.function = function;
	}

	// facility VARBINARY(MAX)
	// parking_info VARBINARY(MAX)
	// Resident _info VARBINARY(MAX)

}