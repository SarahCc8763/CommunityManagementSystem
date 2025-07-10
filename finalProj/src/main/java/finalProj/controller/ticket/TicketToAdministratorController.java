package finalProj.controller.ticket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.ticket.TicketToAdministrator;
import finalProj.dto.ticket.TicketToAdministratorDTO;
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

@PostMapping("/assign")
public List<TicketToAdministrator> assignMultipleVendors(@RequestBody TicketToAdministratorDTO dto) {
    List<TicketToAdministrator> results = new ArrayList<>();
    for (Integer vendorId : dto.getVendorIds()) {
        TicketToAdministrator result = ticketToAdministratorService.create(dto.getTicketId(), vendorId);
        results.add(result);
    }
    return results;
}

	@GetMapping
	public List<TicketToAdministrator> findAll() {
	return ticketToAdministratorService.findAll();
}
	@PutMapping("/ticket-vendors/update/{ticketId}")
public ResponseEntity<Map<String, Object>> updateVendors(
        @PathVariable Integer ticketId,
        @RequestBody List<Integer> vendorIds) {

    ticketToAdministratorService.updateVendors(ticketId, vendorIds);

    Map<String, Object> result = new HashMap<>();
    result.put("success", true);
    result.put("ticketId", ticketId);
    result.put("updatedVendorIds", vendorIds);
    return ResponseEntity.ok(result);
}
}
