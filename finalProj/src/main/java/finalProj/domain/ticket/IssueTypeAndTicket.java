package finalProj.domain.ticket;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "issue_type_and_ticket")
public class IssueTypeAndTicket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@JsonBackReference("ticketAndIssueTypes")
	@ManyToOne
	@JoinColumn(name = "ticket_id")
	private Ticket ticket;// ticket流水號
	
	
	
	@JsonIgnoreProperties({"tickets"})
	@ManyToOne
	@JoinColumn(name = "issue_type_id")
	private IssueType issueType;
	
	

	
	


	@Override
	public String toString() {
		return "IssueTypeAndTicket [id=" + id + ", ticket=" + ticket + ", issueType=" + issueType + "]";
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

	public IssueType getIssueType() {
		return issueType;
	}

	public void setIssueType(IssueType issueType) {
		this.issueType = issueType;
	}// 問題類別流水號

}
