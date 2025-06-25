package finalProj.dto.ticket;

public class FileUploadDTO {

	private String fileName;
	private String base64Data;
	private Integer uploadedBy;
	private Integer ticketId;
	private Integer commentID;

	public Integer getCommentID() {
		return commentID;
	}

	public void setCommentID(Integer commentID) {
		this.commentID = commentID;
	}

	public Integer getTicketId() {
		return ticketId;
	}

	public void setTicketId(Integer ticketId) {
		this.ticketId = ticketId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getBase64Data() {
		return base64Data;
	}

	public void setBase64Data(String base64Data) {
		this.base64Data = base64Data;
	}

	public Integer getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(Integer uploadedBy) {
		this.uploadedBy = uploadedBy;
	}



}
