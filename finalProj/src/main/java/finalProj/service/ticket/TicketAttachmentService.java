package finalProj.service.ticket;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import finalProj.domain.ticket.Ticket;
import finalProj.domain.ticket.TicketAttachment;
import finalProj.domain.ticket.TicketComment;
import finalProj.dto.ticket.FileUploadDTO;
import finalProj.repository.ticket.TicketAttachmentRepository;
import finalProj.repository.ticket.TicketCommentRepository;
import finalProj.repository.ticket.TicketRepository;

@Service
@Transactional
public class TicketAttachmentService {

	@Autowired
	private TicketAttachmentRepository ticketAttachmentRepository;
	@Autowired
	private TicketCommentRepository ticketCommentRepository;
	@Autowired
	private TicketRepository ticketRepository;

	public TicketAttachment saveFile(MultipartFile file, Integer uploadedBy) {
		try {
			TicketAttachment attachment = new TicketAttachment();
			attachment.setFile(file.getBytes());
			attachment.setFileName(file.getOriginalFilename());
			attachment.setUploadedBy(uploadedBy);
			attachment.setUploadedAt(new Date());
			attachment.setTicket(null); // 未關聯 ticket
			attachment.setComment(null); // 未關聯 comment

			return ticketAttachmentRepository.save(attachment);

		} catch (IOException e) {
			throw new RuntimeException("檔案讀取失敗：" + e.getMessage());
		}
	}

	// 上傳
	public TicketAttachment saveBase64(FileUploadDTO dto) {
		try {
			byte[] decoded = Base64.getDecoder().decode(dto.getBase64Data());
			TicketAttachment attachment = new TicketAttachment();
			attachment.setFile(decoded);
			attachment.setFileName(dto.getFileName());
			attachment.setUploadedBy(dto.getUploadedBy());
			attachment.setUploadedAt(new Date());

			// ✅ 擇一設置關聯
			if (dto.getCommentId() != null) {
				TicketComment comment = ticketCommentRepository.findById(dto.getCommentId())
						.orElseThrow(() -> new IllegalArgumentException("找不到留言 ID：" + dto.getCommentId()));
				attachment.setComment(comment); // 關聯留言
			} else if (dto.getTicketId() != null) {
				Ticket ticket = ticketRepository.findById(dto.getTicketId())
						.orElseThrow(() -> new IllegalArgumentException("找不到報修 ID：" + dto.getTicketId()));
				attachment.setTicket(ticket); // 關聯報修單（注意：這是 Integer 欄位）
			} else {
				throw new IllegalArgumentException("ticketId 與 commentId 至少要指定一個");
			}

			return ticketAttachmentRepository.save(attachment);
		} catch (Exception e) {
			throw new RuntimeException("Base64 處理失敗：" + e.getMessage());
		}
	}

	// 修改
	public TicketAttachment update(FileUploadDTO dto, Integer id) {
		byte[] decoded = Base64.getDecoder().decode(dto.getBase64Data());
		TicketAttachment TA = ticketAttachmentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("附件不存在：" + id));
		TA.setFile(decoded);
		TA.setFileName(dto.getFileName());
		TA.setUploadedBy(dto.getUploadedBy());
		TA.setUploadedAt(new Date());

		return ticketAttachmentRepository.save(TA);
	}

	public Optional<TicketAttachment> findById(Integer id) {
		return ticketAttachmentRepository.findById(id);
	}

	public List<TicketAttachment> findAll() {
		return ticketAttachmentRepository.findAll();
	}

	// 刪除附件
	public boolean remove(Integer id) {
		if (id != null) {
			if (ticketAttachmentRepository.existsById(id)) {
				ticketAttachmentRepository.deleteById(id);
				return true;
			}
		}
		return false;
	}

	// 查看圖片
	public MediaType determineMediaType(String fileName) {
		String lower = fileName.toLowerCase();
		if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
			return MediaType.IMAGE_JPEG;
		} else if (lower.endsWith(".png")) {
			return MediaType.IMAGE_PNG;
		} else if (lower.endsWith(".pdf")) {
			return MediaType.APPLICATION_PDF;
		} else if (lower.endsWith(".gif")) {
			return MediaType.IMAGE_GIF;
		}
		return MediaType.APPLICATION_OCTET_STREAM; // fallback 二進位
	}

//	public ResponseEntity<byte[]> buildAttachmentResponse(Integer id){
//		TicketAttachment attachment = ticketAttachmentRepository.findById(id)
//				.orElse(null);
//
//		if (attachment == null) {
//			return null;
//		}
//
//		HttpHeaders headers = new HttpHeaders();
//
//		// 自動推斷檔案類型（可擴充）
//		String fileName = attachment.getFileName().toLowerCase();
//		if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
//			headers.setContentType(MediaType.IMAGE_JPEG);
//		} else if (fileName.endsWith(".png")) {
//			headers.setContentType(MediaType.IMAGE_PNG);
//		} else if (fileName.endsWith(".pdf")) {
//			headers.setContentType(MediaType.APPLICATION_PDF);
//		} else {
//			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//		}
//
//		headers.setContentDisposition(ContentDisposition.inline()
//				.filename(attachment.getFileName())
//				.build());
//
//		return new ResponseEntity<>(attachment.getFile(), headers, HttpStatus.OK); // ✅ 這裡不包 ApiResponse，直接回檔案
//
//	}
}
