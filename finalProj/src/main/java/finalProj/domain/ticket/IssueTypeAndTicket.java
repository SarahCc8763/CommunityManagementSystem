package finalProj.domain.ticket;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "issue_type_and_ticket")
public class IssueTypeAndTicket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "ticket_id")
	private Integer ticketId;// ticket流水號
	@Column(name = "issue_type_id")
	private Integer issueTypeId;// 問題類別流水號

	@Override
	public String toString() {
		return "IssueTypeAndTicket [id=" + id + ", ticketId=" + ticketId + ", issueTypeId=" + issueTypeId + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTicketId() {
		return ticketId;
	}

	public void setTicketId(Integer ticketId) {
		this.ticketId = ticketId;
	}

	public Integer getIssueTypeId() {
		return issueTypeId;
	}

	public void setIssueTypeId(Integer issueTypeId) {
		this.issueTypeId = issueTypeId;
	}

}
