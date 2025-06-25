package finalProj.service.ticket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.ticket.IssueTypeAndTicket;
import finalProj.repository.ticket.IssueTypeAndTicketRepository;

@Service
@Transactional
public class IssueTypeAndTicketService {

	@Autowired
	private IssueTypeAndTicketRepository issueTypeAndTicketRepository;
	
	public IssueTypeAndTicket create(Integer ticketId,Integer issueTypeId) {
		IssueTypeAndTicket issueTypeAndTicket = new IssueTypeAndTicket();
		issueTypeAndTicket.setIssueTypeId(issueTypeId);
		issueTypeAndTicket.setTicketId(ticketId);
		return issueTypeAndTicketRepository.save(issueTypeAndTicket);
	}
	
	public List<IssueTypeAndTicket> findByTicketId(Integer id){
		return issueTypeAndTicketRepository.findByTicketId(id);
	}
}
