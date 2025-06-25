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
@Table(name = "Ticket_issue_Cost_attachment")
public class TicketIssueCostAttachment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;// Cost_attachment流水號

	@JsonBackReference("costAttachment")
	@ManyToOne
	@JoinColumn(name = "ticket_id", nullable = false)
	private Ticket ticket;

	@Column(name = "Cost")
	private Integer cost;// 修繕費用

	@Column(name = "file_name")
	private String fileName;// 檔案名稱

	@Column(name = "[file]")
	private byte[] file;// 檔案

	@Column(name = "Vendor_ID")
	private Integer VendorID;// 廠商id

	@Override
	public String toString() {
		return "TicketIssueCostAttachment [id=" + id + ", ticket=" + ticket + ", cost=" + cost + ", fileName="
				+ fileName + ", file=" + Arrays.toString(file) + ", VendorID=" + VendorID + "]";
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

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
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

	public Integer getVendorID() {
		return VendorID;
	}

	public void setVendorID(Integer vendorID) {
		VendorID = vendorID;
	}

}