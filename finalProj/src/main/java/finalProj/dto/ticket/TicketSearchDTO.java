package finalProj.dto.ticket;

import java.time.LocalDate;
import java.util.List;

public class TicketSearchDTO {
    private String title;
    private String status;
    private LocalDate startDate;
    private Integer reporterId;
    private List<String> issueTypeNames;
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
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public Integer getReporterId() {
		return reporterId;
	}
	public void setReporterId(Integer reporterId) {
		this.reporterId = reporterId;
	}
	public List<String> getIssueTypeNames() {
		return issueTypeNames;
	}
	public void setIssueTypeNames(List<String> issueTypeNames) {
		this.issueTypeNames = issueTypeNames;
	}

    
	

}