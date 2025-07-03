package finalProj.controller.ticket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.ticket.TicketToAdministrator;
import finalProj.service.ticket.TicketToAdministratorService;

@RestController
@RequestMapping("/TicketToAdministrator")
public class TicketToAdministratorController {
	@Autowired
	private TicketToAdministratorService ticketToAdministratorService;

	@PostMapping
	public TicketToAdministrator add(@RequestBody TicketToAdministrator ticketToAdministrator) {
		return ticketToAdministratorService.create(ticketToAdministrator.getTicketId(),
				ticketToAdministrator.getVendorId());
	}

	@GetMapping("/ticket/{ticketId}")
	public List<TicketToAdministrator> findByTicketId(@PathVariable Integer ticketId) {
		return ticketToAdministratorService.findByTicketId(ticketId);
	}

	@GetMapping("/vendor/{vendorId}")
	public List<TicketToAdministrator> findByVendorId(@PathVariable Integer vendorId) {
		return ticketToAdministratorService.findByVendorId(vendorId);
	}

	// DTO for request body
	public static class TicketToAdministratorRequest {
		// private Integer ticketId;
		// private Integer vendorId;
		// getter / setter
	}
}
