package finalProj.controller.ticket;

import java.util.ArrayList;
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

import finalProj.domain.ticket.TicketIssueCostAttachment;
import finalProj.dto.ticket.ApiResponse;
import finalProj.dto.ticket.TicketIssueCostAttachmentDTO;
import finalProj.service.ticket.TicketIssueCostAttachmentService;

@RestController
@RequestMapping("/TicketIssueCostAttachment")
public class TicketIssueCostAttachmentController {

	@Autowired
	TicketIssueCostAttachmentService ticketIssueCostAttachmentService;

	// 上傳金額附件
	@PostMapping("/upload/base64/multiple")
	public ApiResponse<List<Integer>> uploadMultipleBase64(
			@RequestBody List<TicketIssueCostAttachmentDTO> files) {
		List<Integer> savedIds = new ArrayList<>();
		try {
			for (TicketIssueCostAttachmentDTO dto : files) {
				TicketIssueCostAttachment saved = ticketIssueCostAttachmentService.saveBase64(
						dto.getTicketId(),
						dto.getCost(),
						dto.getVendorID(),
						dto.getFileName(),
						dto.getBase64Data());

				savedIds.add(saved.getId());
			}
			return new ApiResponse<>(true, "上傳成功", savedIds);
		} catch (Exception e) {
			return new ApiResponse<>(false, "上傳失敗：" + e.getMessage(), null);
		}
	}

	// 修改金額附件
	@PutMapping("/update/base64/{id}")
	public ApiResponse<Integer> updateBase64(
			@PathVariable("id") Integer id,
			@RequestBody TicketIssueCostAttachmentDTO dto) {
		System.out.println("id from URL = " + id);
		try {
			TicketIssueCostAttachment updated = ticketIssueCostAttachmentService.update(
					dto.getTicketId(),
					dto.getCost(),
					dto.getVendorID(),
					dto.getFileName(),
					dto.getBase64Data(),
					id);

			return new ApiResponse<>(true, "更新成功", updated.getId());
		} catch (Exception e) {
			return new ApiResponse<>(false, "更新失敗：" + e.getMessage(), null);
		}
	}
	//查詢ticket下的所有金額附件
	@GetMapping("/ticket/{ticketId}")
	public List<TicketIssueCostAttachment> findByTicketId(@PathVariable("ticketId") Integer ticketId) {
		return ticketIssueCostAttachmentService.findByTicketId(ticketId);
	}
	
	// 刪除留言
	@DeleteMapping("/{id}")
	public boolean delete(@PathVariable("id") Integer id) {
		// TODO: 呼叫 service.delete(id)
		boolean result = ticketIssueCostAttachmentService.remove(id);
		return result;
	}
	//查詢所有金額附件
	@GetMapping("/All")
	public List<TicketIssueCostAttachment> findAll() {
		return ticketIssueCostAttachmentService.findAll();
	}
	

}
