package finalProj.service.ticket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domin.ticket.TicketToAdministrator;
import finalProj.repository.ticket.TicketToAdministratorRepository;

@Service
@Transactional
public class TicketToAdministratorService {
	
	@Autowired
	private TicketToAdministratorRepository ticketToAdministratorRepository;
	
	public TicketToAdministrator create(Integer ticketId,Integer vendorId) {
		TicketToAdministrator ticketToAdministrator = new TicketToAdministrator();
		ticketToAdministrator.setVendorId(vendorId);
		ticketToAdministrator.setTicketId(ticketId);
		return ticketToAdministratorRepository.save(ticketToAdministrator);
	}
	
	public List<TicketToAdministrator> findByTicketId(Integer id){
		return ticketToAdministratorRepository.findByTicketId(id);
	}
	
	public List<TicketToAdministrator> findByVendorId(Integer id){
		return ticketToAdministratorRepository.findByVendorId(id);
	}

}
