package finalProj.dto.ticket;

import java.util.List;

public class TicketToAdministratorDTO  {
    
    private Integer ticketId;
	private List<Integer> vendorIds;

	public Integer getTicketId() {
		return ticketId;
	}

	public void setTicketId(Integer ticketId) {
		this.ticketId = ticketId;
	}

    public List<Integer> getVendorIds() {
        return vendorIds;
    }

    public void setVendorIds(List<Integer> vendorIds) {
        this.vendorIds = vendorIds;
    }
    
}
