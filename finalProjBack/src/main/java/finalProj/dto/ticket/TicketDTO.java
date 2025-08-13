package finalProj.dto.ticket;

import java.util.List;

public class TicketDTO {

	private Integer reporterId;
	private String title;
	private Integer assignerId;
	private String status;
	private String issueDescription;
	private Integer actionBy;
	private Integer communityId;
	private String notes;
	private List<String> issueTypeNames;
	private List<Integer> attachmentIds;
	private java.util.Date actionTime;

	public List<String> getIssueTypeNames() {
		return issueTypeNames;
	}

	public void setIssueTypeNames(List<String> issueTypeNames) {
		this.issueTypeNames = issueTypeNames;
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

	public List<Integer> getAttachmentIds() {
		return attachmentIds;
	}

	public void setAttachmentIds(List<Integer> attachmentIds) {
		this.attachmentIds = attachmentIds;
	}

	public Integer getReporterId() {
		return reporterId;
	}

	public void setReporterId(Integer reporterId) {
		this.reporterId = reporterId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getAssignerId() {
		return assignerId;
	}

	public void setAssignerId(Integer assignerId) {
		this.assignerId = assignerId;
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

	public Integer getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Integer communityId) {
		this.communityId = communityId;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

}
