package finalProj.controller.ticket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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
	
	@PutMapping("/update/{ticketId}")
	public ResponseEntity<?> updateTicketIssueTypes(
	        @PathVariable("ticketId") Integer ticketId,
	        @RequestBody List<Integer> issueTypeIds) {

	    try {
	    	issueTypeAndTicketService.updateTicketIssueTypes(ticketId, issueTypeIds);
	        return ResponseEntity.ok("問題種類已更新");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("更新失敗：" + e.getMessage());
	    }
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
