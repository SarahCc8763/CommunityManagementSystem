package finalProj.domain.community;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import finalProj.domain.bulletin.Bulletin;
import finalProj.domain.bulletin.BulletinCategory;
import finalProj.domain.faq.FaqCategory;
import finalProj.domain.feedback.Feedback;
import finalProj.domain.feedback.FeedbackCategory;

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

	// @JsonManagedReference("community")
	// @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval =
	// true)
	// private List<Ticket> tickets;

	@Column(name = "name")
	private String name; // 社區名稱

	@Column(name = "address")
	private String address; // 地址

	@Column(name = "create_time", insertable = false, updatable = false)
	private java.util.Date createTime; // 創建時間

	@Column(name = "[function]") // SQL Server 保留字，需用中括號轉義
	private Integer function; // 使用功能

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

	@Override
	public String toString() {
		return "community [ID=" + communityId + ", name=" + name + ", address=" + address + ", createTime=" + createTime
				+ ", function=" + function + "]";
	}

	public Integer getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Integer iD) {
		communityId = iD;
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

	public Integer getFunction() {
		return function;
	}

	public void setFunction(Integer function) {
		this.function = function;
	}

	// public List<Ticket> getTickets() {
	// return tickets;
	// }

	// public void setTickets(List<Ticket> tickets) {
	// this.tickets = tickets;
	// }

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

	// facility VARBINARY(MAX)
	// parking_info VARBINARY(MAX)
	// Resident _info VARBINARY(MAX)

}