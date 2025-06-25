package finalProj.domain.ticket;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ticket_attachment")
public class TicketAttachment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;// ticket_attachment流水號

	@JsonBackReference("attachments")
	@ManyToOne
	@JoinColumn(name = "comment_id", nullable = true)
	private TicketComment comment;

	@JsonBackReference("attachments")
	@ManyToOne
	@JoinColumn(name = "ticket_id", nullable = true)
	private Ticket ticket;

	@Override
	public String toString() {
		return "TicketAttachment [id=" + id + ", comment=" + comment + ", ticket=" + ticket + ", uploadedBy="
				+ uploadedBy + ", fileName=" + fileName + ", file=" + Arrays.toString(file) + ", imageUrl1="
				+ Arrays.toString(imageUrl1) + ", uploadedAt=" + uploadedAt + "]";
	}

	@Column(name = "uploaded_by")
	private Integer uploadedBy;

	@Column(name = "file_name")
	private String fileName;// 檔案name

	@Column(name = "[file]")
	private byte[] file;// 檔案

	@Column(name = "image_url1")
	private byte[] imageUrl1;// 圖

	@Column(name = "uploaded_at")
	private java.util.Date uploadedAt;// 上傳時間

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TicketComment getComment() {
		return comment;
	}

	public void setComment(TicketComment comment) {
		this.comment = comment;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public Integer getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(Integer uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public byte[] getImageUrl1() {
		return imageUrl1;
	}

	public void setImageUrl1(byte[] imageUrl1) {
		this.imageUrl1 = imageUrl1;
	}

	public java.util.Date getUploadedAt() {
		return uploadedAt;
	}

	public void setUploadedAt(java.util.Date uploadedAt) {
		this.uploadedAt = uploadedAt;
	}

}
