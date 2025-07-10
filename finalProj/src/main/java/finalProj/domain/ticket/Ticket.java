package finalProj.domain.ticket;

import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import finalProj.domain.community.Community;
import finalProj.domain.users.Users;
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
@Table(name = "ticket")
public class Ticket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id; // ticket流水號

	@JsonBackReference("communityTicket")
	@ManyToOne
	@JoinColumn(name = "community_id", nullable = false, referencedColumnName = "id")
	private Community community;// (社區)多對一(ticket)

	@JsonManagedReference("ticketComments")
	@OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TicketComment> comments;// (ticket)一對多(留言)

	@JsonManagedReference("ticketAndIssueTypes")
	@OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<IssueTypeAndTicket> issueTypes;

	@JsonManagedReference("ticketAttachment")
	@OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TicketAttachment> attachments;// (ticket)一對多(附件)

	@JsonManagedReference("ticketCostAttachment")
	@OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TicketIssueCostAttachment> costAttachment;// (ticket)一對多(金額附件)

	// 報修人（使用者）
	@ManyToOne
	@JoinColumn(name = "reporter_id", nullable = true, referencedColumnName = "users_id")
	@JsonBackReference("reporterUser")
	private Users reporter;

	@Transient
	private String name;

	// 被指派處理的管理員
	@JsonBackReference("assignerIdTicket")
	@ManyToOne
	@JoinColumn(name = "assigner_id", nullable = true, referencedColumnName = "users_id")
	private Users assignerId;

//	@Transient
//	private String assignerName;

	@Column(name = "title")
	private String title; // 問題標題

	@Column(name = "[status]") // 保留字加中括號
	private String status; // 問題狀態

	@Column(name = "issue_description")
	private String issueDescription; // 問題敘述

	@Column(name = "Cost")
	private Integer cost; // 修繕費用（屬性小寫 cost）

	@Column(name = "action_time", nullable = true)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "GMT+8")
	private java.util.Date actionTime; // 更改時間

	@Column(name = "action_by")
	private Integer actionBy; // 誰更動的

	@CreationTimestamp // Hibernate annotation 自動填入當前時間
	@Column(name = "start_date", nullable = false, updatable = false)
	private java.util.Date startDate; // ticket創建時間

	@Column(name = "end_date")
	private java.util.Date endDate; // ticket結束時間

	@Column(name = "notes")
	private String notes; // 備註

	@Override
	public String toString() {
		return "Ticket [id=" + id + ", community=" + community + ", comments=" + comments + ", issueTypes=" + issueTypes
				+ ", attachments=" + attachments + ", costAttachment=" + costAttachment + ", reporter=" + reporter
				+ ", assignerId=" + assignerId + ", title=" + title + ", status=" + status + ", issueDescription="
				+ issueDescription + ", cost=" + cost + ", actionTime=" + actionTime + ", actionBy=" + actionBy
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", notes=" + notes + "]";
	}

	public List<IssueTypeAndTicket> getIssueTypes() {
		return issueTypes;
	}

	public void setIssueTypes(List<IssueTypeAndTicket> issueTypes) {
		this.issueTypes = issueTypes;
	}

//	public String getAssignerName() {
//		if (reporter != null) {
//			assignerName = assignerId.getName();
//		}
//		return assignerName;
//	}
//
//	public void setAssignerName(String assignerName) {
//		this.assignerName = assignerName;
//	}

	public String getName() {
		if (reporter != null) {
			name = reporter.getName();
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Users getAssignerId() {
		return assignerId;
	}

	public void setAssignerId(Users assignerId) {
		this.assignerId = assignerId;
	}

	public Users getReporter() {
		return reporter;
	}

	public void setReporter(Users reporter) {
		this.reporter = reporter;
	}

	public List<TicketAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<TicketAttachment> attachments) {
		this.attachments = attachments;
	}

	public List<TicketIssueCostAttachment> getCostAttachment() {
		return costAttachment;
	}

	public void setCostAttachment(List<TicketIssueCostAttachment> costAttachment) {
		this.costAttachment = costAttachment;
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

	public List<TicketComment> getComments() {
		return comments;
	}

	public void setComments(List<TicketComment> comments) {
		this.comments = comments;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIssueDescription() {
		return issueDescription;
	}

	public void setIssueDescription(String issueDescription) {
		this.issueDescription = issueDescription;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}

	public java.util.Date getActionTime() {
		return actionTime;
	}

	public void setActionTime(java.util.Date actionTime) {
		this.actionTime = actionTime;
	}

	public Integer getActionBy() {
		return actionBy;
	}

	public void setActionBy(Integer actionBy) {
		this.actionBy = actionBy;
	}

	public java.util.Date getStartDate() {
		return startDate;
	}

	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}

	public java.util.Date getEndDate() {
		return endDate;
	}

	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

}
