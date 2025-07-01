package finalProj.controller.ticket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.ticket.IssueTypeAndTicket;
import finalProj.service.ticket.IssueTypeAndTicketService;

@RestController
@RequestMapping("/ticket-issue")
public class IssueTypeAndTicketController {

	@Autowired
	private IssueTypeAndTicketService issueTypeAndTicketService;
	
	@GetMapping
	public List<IssueTypeAndTicket> findAll(){
		return issueTypeAndTicketService.findAll();
	}

//	@PostMapping
//	public IssueTypeAndTicket add(@RequestBody IssueTypeAndTicket issueTypeAndTicket) {
//		return issueTypeAndTicketService.create(issueTypeAndTicket.getTicket(), issueTypeAndTicket.getIssueType());
//	}
//
//	@GetMapping("/ticket/{ticketId}")
//	public List<IssueTypeAndTicket> findByTicket(@PathVariable Integer ticketId) {
//		return issueTypeAndTicketService.findByTicket(ticketId);
//	}
//
//	// DTO for request body
//	public static class TicketIssueRequest {
//		private Integer ticketId;
//		private Integer issueTypeId;
//		// getter / setter
//	}
}
