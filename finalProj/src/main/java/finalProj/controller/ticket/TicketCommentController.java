package finalProj.controller.ticket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.ticket.TicketComment;
import finalProj.dto.ticket.ApiResponse;
import finalProj.dto.ticket.CommentDTO;
import finalProj.service.ticket.TicketCommentService;

@RestController
@RequestMapping("/TicketComment")
public class TicketCommentController {

	@Autowired
	private TicketCommentService ticketCommentService;

	// 新增留言
	@PostMapping
	public ApiResponse<TicketComment> addComment(@RequestBody CommentDTO commentDTO) {
		if (commentDTO.getTicketId() == null) {
			return new ApiResponse<>(false, "缺少報修單 ID", null);
		}
		if (commentDTO.getCommenterId() == null) {
			return new ApiResponse<>(false, "此人不存在", null);
		}

		try {
			TicketComment saved = ticketCommentService.save(commentDTO);
			return new ApiResponse<>(true, "留言成功", saved);
		} catch (IllegalArgumentException e) {
			return new ApiResponse<>(false, e.getMessage(), null);
		}
	}

	// 查詢所有留言
	@GetMapping
	public List<TicketComment> findAll() {
		return ticketCommentService.findAll();
	}

	// 查詢某張報修單底下的所有留言
	@GetMapping("/ticket/{ticketId}")
	public List<TicketComment> findByTicketId(@PathVariable("ticketId") Integer ticketId) {
		return ticketCommentService.findByTicketId(ticketId);
	}

	// 刪除留言
	@DeleteMapping("/{id}")
	public boolean delete(@PathVariable("id") Integer id) {
		// TODO: 呼叫 service.delete(id)
		boolean result = ticketCommentService.remove(id);
		return result;
	}

	// 更新留言
	@PutMapping("/{id}")
	public ApiResponse<TicketComment> update(@PathVariable("id") Integer id,
	                                         @RequestBody CommentDTO commentDTO) {
	    try {
	        TicketComment result = ticketCommentService.update(id, commentDTO);
	        return new ApiResponse<>(true, "留言更新成功", result);
	    } catch (IllegalArgumentException e) {
	        return new ApiResponse<>(false, e.getMessage(), null);
	    }
	}

}
