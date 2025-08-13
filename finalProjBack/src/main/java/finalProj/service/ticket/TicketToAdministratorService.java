package finalProj.service.ticket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.ticket.TicketToAdministrator;
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

	public List<TicketToAdministrator> findAll() {
		return ticketToAdministratorRepository.findAll();
	}

	public void updateVendors(Integer ticketId, List<Integer> vendorIds) {
        // 先刪除原有廠商紀錄
        ticketToAdministratorRepository.deleteByTicketId(ticketId);

        // 重新插入新的廠商資料
        for (Integer vendorId : vendorIds) {
            TicketToAdministrator entity = new TicketToAdministrator();
            entity.setTicketId(ticketId);
            entity.setVendorId(vendorId);
            ticketToAdministratorRepository.save(entity);
        }
    }
}
