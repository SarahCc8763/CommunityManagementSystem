package finalProj.domain.community;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import finalProj.domain.bulletin.Bulletin;
import finalProj.domain.bulletin.BulletinCategory;
import finalProj.domain.faq.Faq;
import finalProj.domain.faq.FaqCategory;
import finalProj.domain.feedback.Feedback;
import finalProj.domain.feedback.FeedbackCategory;
import finalProj.domain.packages.Packages;
import finalProj.domain.ticket.Ticket;
import finalProj.domain.users.Units;
import finalProj.domain.users.Users;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "community")
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

	@JsonManagedReference("communityTicket")
	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Ticket> tickets;

	@JsonManagedReference("communityUser")
	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Users> users;

	@JsonManagedReference("communityUnit")
	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Units> units;

	@JsonManagedReference("communityPackage")
	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Packages> packagesList;

	// --- 政宇的關聯 START ---
	// 社區-公告
	@OneToMany(mappedBy = "community")
	@JsonManagedReference("community-bulletin")
	private List<Bulletin> bulletins;
	// 社區-公告分類
	@OneToMany(mappedBy = "community")
	@JsonManagedReference("community-bulletin-category")
	private List<BulletinCategory> bulletinCategories;
	// 社區-關鍵字分類
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

	// --- 政宇的關聯 END ---

	@Override
	public String toString() {
		return "Community [communityId=" + communityId + ", tickets=" + tickets + ", name=" + name + ", address="
				+ address + ", createTime=" + createTime + ", function=" + function + "]";
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