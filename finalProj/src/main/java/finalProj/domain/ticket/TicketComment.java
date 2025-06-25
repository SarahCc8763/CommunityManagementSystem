package finalProj.domain.ticket;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@Table(name = "ticket_comment")
public class TicketComment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;// ticket_comment流水號

	@JsonBackReference("comments")
	@ManyToOne
	@JoinColumn(name = "ticket_id", nullable = false)
	private Ticket ticket;

	@JsonManagedReference("comment")
	@OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TicketAttachment> attachments;

	@Column(name = "comment_time", insertable = false, updatable = false)
	private java.util.Date commentTime;// 留言時間

	@Column(name = "commenter_id")
	private Integer commenterId;// 留言ID

	@Column(name = "comment")
	private String comment;// 留言

	@Override
	public String toString() {
		return "TicketComment [id=" + id + ", ticket=" + ticket + ", attachments=" + attachments + ", commentTime="
				+ commentTime + ", commenterId=" + commenterId + ", comment=" + comment + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public List<TicketAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<TicketAttachment> attachments) {
		this.attachments = attachments;
	}

	public java.util.Date getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(java.util.Date commentTime) {
		this.commentTime = commentTime;
	}

	public Integer getCommenterId() {
		return commenterId;
	}

	public void setCommenterId(Integer commenterId) {
		this.commenterId = commenterId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
