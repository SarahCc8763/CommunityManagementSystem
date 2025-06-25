package finalProj.service.ticket;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import finalProj.domin.ticket.Ticket;
import finalProj.domin.ticket.TicketAttachment;
import finalProj.domin.ticket.TicketComment;
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
	//上傳	
	public TicketAttachment saveBase64(String fileName, String base64Data, Integer uploadedBy, Integer ticketId,
			Integer commentID) {
		try {
			byte[] decoded = Base64.getDecoder().decode(base64Data);
			TicketAttachment attachment = new TicketAttachment();
			attachment.setFile(decoded);
			attachment.setFileName(fileName);
			attachment.setUploadedBy(uploadedBy);
			attachment.setUploadedAt(new Date());

			// ✅ 擇一設置關聯
			if (commentID != null) {
				TicketComment comment = ticketCommentRepository.findById(commentID)
						.orElseThrow(() -> new IllegalArgumentException("找不到留言 ID：" + commentID));
				attachment.setComment(comment); // 關聯留言
			} else if (ticketId != null) {
				Ticket ticket = ticketRepository.findById(ticketId)
						.orElseThrow(() -> new IllegalArgumentException("找不到報修 ID：" + ticketId));
				attachment.setTicket(ticket); // 關聯報修單（注意：這是 Integer 欄位）
			} else {
				throw new IllegalArgumentException("ticketId 與 commentId 至少要指定一個");
			}

			return ticketAttachmentRepository.save(attachment);
		} catch (Exception e) {
			throw new RuntimeException("Base64 處理失敗：" + e.getMessage());
		}
	}
	
	//修改
	public TicketAttachment update(String fileName, String base64Data, Integer uploadedBy, Integer ticketId,
			Integer commentID, Integer id) {
		byte[] decoded = Base64.getDecoder().decode(base64Data);
		TicketAttachment TA = ticketAttachmentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("附件不存在：" + id));
		TA.setFile(decoded);
		TA.setFileName(fileName);
		TA.setUploadedBy(uploadedBy);
		TA.setUploadedAt(new Date());
		
		return ticketAttachmentRepository.save(TA);
	}

	public Optional<TicketAttachment> findById(Integer id) {
		return ticketAttachmentRepository.findById(id);
	}

	public List<TicketAttachment> findAll() {
		return ticketAttachmentRepository.findAll();
	}
	
	//刪除附件
	public boolean remove(Integer id) {
		if (id != null) {
			if (ticketAttachmentRepository.existsById(id)) {
				ticketAttachmentRepository.deleteById(id);
				return true;
			}
		}
		return false;
	}

}
