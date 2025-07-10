package finalProj.domain.ticket;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "issue_type")
public class IssueType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer Id;// 問題類別流水號
	@Column(name = "issue_type_name")
	private String issueTypeName;// 問題類別名稱

	// @JsonManagedReference("issueTypesAndTicket")
	@OneToMany(mappedBy = "issueType", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<IssueTypeAndTicket> tickets;

	@Override
	public String toString() {
		return "IssueType [Id=" + Id + ", issueTypeName=" + issueTypeName + ", tickets=" + tickets + "]";
	}

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getIssueTypeName() {
		return issueTypeName;
	}

	public void setIssueTypeName(String issueTypeName) {
		this.issueTypeName = issueTypeName;
	}

	public List<IssueTypeAndTicket> getTickets() {
		return tickets;
	}

	public void setTickets(List<IssueTypeAndTicket> tickets) {
		this.tickets = tickets;
	}

}
