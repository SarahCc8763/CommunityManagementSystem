package finalProj.domin.ticket;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ticket_to_administrator")
public class TicketToAdministrator {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;// 流水號

	@Column(name = "ticket_id")
	private Integer ticketId;// ticket流水號

	@Column(name = "vendor_ID")
	private Integer vendorId;// 廠商id
	
	

	@Override
	public String toString() {
		return "TicketToAdministrator [id=" + id + ", ticketId=" + ticketId + ", vendorId=" + vendorId + "]";
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

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	
}