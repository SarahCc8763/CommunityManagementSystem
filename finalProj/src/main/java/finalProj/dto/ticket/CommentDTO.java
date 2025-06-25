package finalProj.dto.ticket;



public class CommentDTO {

	private Integer id;// ticket_comment流水號

	private Integer ticketId;// ticket流水號

	private java.util.Date commentTime;// 留言時間

	private Integer commenterId;// 留言ID

	private String comment;// 留言

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
