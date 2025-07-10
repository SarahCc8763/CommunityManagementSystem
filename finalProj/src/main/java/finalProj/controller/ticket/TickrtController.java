package finalProj.controller.ticket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.ticket.Ticket;
import finalProj.dto.ticket.ApiResponse;
import finalProj.dto.ticket.TicketDTO;
import finalProj.dto.ticket.TicketSearchDTO;
import finalProj.service.ticket.TicketService;

@RestController
@RequestMapping("/ticket")
public class TickrtController {
	@Autowired
	private TicketService ticketService;

	// Create
	@PostMapping
	public ApiResponse<Ticket> createTicket(@RequestBody TicketDTO dto) {
		if (dto.getCommunityId() == null) {
			return new ApiResponse<>(false, "缺少社區 ID", null);
		}
		if (dto.getReporterId() == null) {
			return new ApiResponse<>(false, "此人不存在", null);
		}

		try {
			Ticket saved = ticketService.save(dto);
			return new ApiResponse<>(true, "報修單建立成功", saved);
		} catch (IllegalArgumentException e) {
			return new ApiResponse<>(false, e.getMessage(), null);
		}
	}

	// Findall
	@GetMapping
	public List<Ticket> findAll() {
		// 呼叫 service.findAll()
		return ticketService.findAll(); // 空清單回傳
	}

	// Findbyid
	@GetMapping("/{id}")
	public Ticket findById(@PathVariable("id") Integer id) {
		// 呼叫 service.findById(id)
		Ticket result = ticketService.findById(id);
		return result;
	}

	// Update
	@PutMapping("/{id}")
	public ApiResponse<Ticket> update(@PathVariable("id") Integer id, @RequestBody TicketDTO dto) {
		try {
			Ticket result = ticketService.update(id, dto);
			return new ApiResponse<>(true, "更新成功", result);
		} catch (IllegalArgumentException e) {
			return new ApiResponse<>(false, e.getMessage(), null);
		}
	}

	// Delete
	@DeleteMapping("/{id}")
	public boolean delete(@PathVariable("id") Integer id) {
		// 呼叫 service.delete(id)
		boolean result = ticketService.remove(id);
		return result;
	}

	// 多條件查詢
	@PostMapping("/search")
	public List<Ticket> searchTickets(@RequestBody TicketSearchDTO dto) {
		return ticketService.searchTickets(dto);
	}

//只改狀態
	@PutMapping("/status/{id}")
public ApiResponse<Ticket> updateStatus(@PathVariable("id") Integer id, @RequestBody TicketDTO dto) {
	try {
		Ticket result = ticketService.updateStatusOnly(id, dto);
		return new ApiResponse<>(true, "狀態更新成功", result);
	} catch (IllegalArgumentException e) {
		return new ApiResponse<>(false, e.getMessage(), null);
	}
}


	//分頁功能
// 	@GetMapping("/ticketsPage")
// public Page<Ticket> findAllPaged(
//     @RequestParam(defaultValue = "0") int page,
//     @RequestParam(defaultValue = "10") int size
// ) {
//     return ticketService.findAllPages(page, size);
// }
}
