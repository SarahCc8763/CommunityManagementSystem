package finalProj.controller.ticket;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import finalProj.domain.ticket.TicketAttachment;
import finalProj.dto.ticket.ApiResponse;
import finalProj.dto.ticket.FileUploadDTO;
import finalProj.service.ticket.TicketAttachmentService;

@RestController
@RequestMapping("/ticket-attachment")
public class TicketAttachmentController {

	@Autowired
	private TicketAttachmentService ticketAttachmentService;

	@PostMapping("/upload")
	public ApiResponse<Integer> uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam("uploadedBy") Integer userId) {
		try {
			TicketAttachment saved = ticketAttachmentService.saveFile(file, userId);
			return new ApiResponse<>(true, "上傳成功", saved.getId());
		} catch (Exception e) {
			return new ApiResponse<>(false, e.getMessage(), null);
		}
	}

	// 上傳圖片(base64)
	@PostMapping("/upload/base64/multiple")
	public ApiResponse<List<Integer>> uploadMultipleBase64(@RequestBody List<FileUploadDTO> files) {
		List<Integer> savedIds = new ArrayList<>();
		try {
			for (FileUploadDTO dto : files) {
				TicketAttachment saved = ticketAttachmentService.saveBase64(dto);
				savedIds.add(saved.getId());
			}
			return new ApiResponse<>(true, "上傳成功", savedIds);
		} catch (Exception e) {
			return new ApiResponse<>(false, "上傳失敗：" + e.getMessage(), null);
		}
	}

	// 修改圖片(base64)
	@PutMapping("/update/base64/{id}")
	public ApiResponse<Integer> updateBase64(@PathVariable("id") Integer id, @RequestBody FileUploadDTO dto) {
		try {
			TicketAttachment updated = ticketAttachmentService.update(dto, id);

			return new ApiResponse<>(true, "更新成功", updated.getId());
		} catch (Exception e) {
			return new ApiResponse<>(false, "更新失敗：" + e.getMessage(), null);
		}
	}

	// 查看附件內容
	@GetMapping("/view/{id}")
	public ResponseEntity<?> viewAttachment(@PathVariable("id") Integer id) {
		TicketAttachment attachment = ticketAttachmentService.findById(id).orElse(null);
		if (attachment == null) {
			// 這裡不設 Content-Type，Spring 會自動用 JSON 處理
			return ResponseEntity.ok(new ApiResponse<>(false, "找不到附件 ID：" + id, null));
		}
		// 只有在有檔案時才設定 Content-Type 為 octet-stream 或 image
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(ticketAttachmentService.determineMediaType(attachment.getFileName()));
		headers.setContentDisposition(ContentDisposition.inline()
			.filename(attachment.getFileName())
			.build());

		return new ResponseEntity<>(attachment.getFile(), headers, HttpStatus.OK);
	}

//	@GetMapping("/view/{id}")
//	public ResponseEntity<?> viewAttachment(@PathVariable("id") Integer id) {
//		ResponseEntity<byte[]> response = ticketAttachmentService.buildAttachmentResponse(id);
//
//		if (response == null) {
//			ApiResponse<byte[]> error = new ApiResponse<>(false, "找不到附件" + id, null);
//			return ResponseEntity.ok(error);
//		}
//		return response;
//	}
	
	

	// 查看所有附件
	@GetMapping("/All")
	public ApiResponse<List<TicketAttachment>> findAllAttachment() {
		try {
			List<TicketAttachment> list = ticketAttachmentService.findAll();
			return new ApiResponse<>(true, "取得所有附件成功", list);
		} catch (Exception e) {
			return new ApiResponse<>(false, "查詢失敗：" + e.getMessage(), null);
		}
	}

	// 刪除附件
	@DeleteMapping("{id}")
	public boolean reomve(@PathVariable("id") Integer id) {
		if (id != null) {
			return ticketAttachmentService.remove(id);
		} else {

			return false;
		}
	}

}
