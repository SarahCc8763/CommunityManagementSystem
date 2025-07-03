package finalProj.dto.ticket;

import java.util.List;

public class TicketSearchDTO {
	private Integer id;
	private Integer reporterId;
	private Integer assignerId;
	private String title;
	private List<String> issueTypeNames;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReporterId() {
		return reporterId;
	}

	public void setReporterId(Integer reporterId) {
		this.reporterId = reporterId;
	}

	public Integer getAssignerId() {
		return assignerId;
	}

	public void setAssignerId(Integer assignerId) {
		this.assignerId = assignerId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getIssueTypeNames() {
		return issueTypeNames;
	}

	public void setIssueTypeNames(List<String> issueTypeNames) {
		this.issueTypeNames = issueTypeNames;
	}

}
