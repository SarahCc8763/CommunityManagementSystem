package finalProj.service.ticket;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.ticket.IssueType;
import finalProj.domain.ticket.IssueTypeAndTicket;
import finalProj.domain.ticket.Ticket;
import finalProj.repository.ticket.IssueTypeAndTicketRepository;
import finalProj.repository.ticket.IssueTypeRepository;
import finalProj.repository.ticket.TicketRepository;

@Service
@Transactional
public class IssueTypeAndTicketService {
	
	@Autowired
	private TicketRepository ticketRepository;
	@Autowired
	private IssueTypeAndTicketRepository issueTypeAndTicketRepository;
	@Autowired
	private IssueTypeRepository issueTypeRepository;

	public List<IssueTypeAndTicket> findAll() {
		return issueTypeAndTicketRepository.findAll();
	}
	public void updateTicketIssueTypes(Integer ticketId, List<Integer> issueTypeIds) {
	    // 1. 刪除該 ticket 所有舊的對應
		issueTypeAndTicketRepository.deleteByTicketId(ticketId);

	    // 2. 建立新的關聯
	    List<IssueTypeAndTicket> newRelations = new ArrayList<>();
	    for (Integer typeId : issueTypeIds) {
	        Ticket ticket = ticketRepository.findById(ticketId)
	                .orElseThrow(() -> new IllegalArgumentException("Ticket 不存在"));

	        IssueType issueType = issueTypeRepository.findById(typeId)
	                .orElseThrow(() -> new IllegalArgumentException("IssueType 不存在"));

	        IssueTypeAndTicket relation = new IssueTypeAndTicket();
	        relation.setTicket(ticket);
	        relation.setIssueType(issueType);
	        newRelations.add(relation);
	    }

	    issueTypeAndTicketRepository.saveAll(newRelations);
	}

}
