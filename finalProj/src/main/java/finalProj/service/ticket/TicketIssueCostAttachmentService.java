package finalProj.service.ticket;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domin.ticket.Ticket;
import finalProj.domin.ticket.TicketIssueCostAttachment;
import finalProj.repository.ticket.TicketIssueCostAttachmentRepository;
import finalProj.repository.ticket.TicketRepository;

@Service
@Transactional
public class TicketIssueCostAttachmentService {

	@Autowired
	private TicketIssueCostAttachmentRepository ticketIssueCostAttachmentRepository;
	@Autowired
	private TicketRepository ticketRepository;
	//上傳金額附件
	public TicketIssueCostAttachment saveBase64(Integer ticketId, Integer cost, Integer VendorID,String fileName,
			String base64Data) {
		try {
			byte[] decoded = Base64.getDecoder().decode(base64Data);
			TicketIssueCostAttachment TICA = new TicketIssueCostAttachment();
			TICA.setFile(decoded);
			TICA.setFileName(fileName);
			TICA.setCost(cost);
			Ticket ticket = ticketRepository.findById(ticketId)
					.orElseThrow(() -> new IllegalArgumentException("找不到報修 ID：" + ticketId));
			TICA.setTicket(ticket);
			TICA.setVendorID(VendorID);

			return ticketIssueCostAttachmentRepository.save(TICA);
		} catch (Exception e) {
			throw new RuntimeException("Base64 處理失敗：" + e.getMessage());
		}
	}
	
	//修改金額附件
	public TicketIssueCostAttachment update(Integer ticketId, Integer cost, Integer VendorID,String fileName,
			String base64Data,Integer id) {
		byte[] decoded = Base64.getDecoder().decode(base64Data);
		TicketIssueCostAttachment TICA = ticketIssueCostAttachmentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("金額附件不存在：" + id));
		TICA.setFile(decoded);
		TICA.setFileName(fileName);
		TICA.setCost(cost);
		Ticket ticket = ticketRepository.findById(ticketId)
				.orElseThrow(() -> new IllegalArgumentException("找不到報修 ID：" + ticketId));
		TICA.setTicket(ticket);
		TICA.setVendorID(VendorID);
		
		return ticketIssueCostAttachmentRepository.save(TICA);
	}

	public Optional<TicketIssueCostAttachment> findById(Integer id) {
		return ticketIssueCostAttachmentRepository.findById(id);
	}
	//查詢所有金額附件
	public List<TicketIssueCostAttachment> findAll() {
		return ticketIssueCostAttachmentRepository.findAll();
	}
	
	// 查某張 ticket 的所有金額附件
	public List<TicketIssueCostAttachment> findByTicketId(Integer ticketId) {
		return ticketIssueCostAttachmentRepository.findByTicketId(ticketId);
	}
	
	// 刪除金額附件
	public boolean remove(Integer id) {
		if (id!=null) {
			Optional<TicketIssueCostAttachment> opt = ticketIssueCostAttachmentRepository.findById(id);
			if(opt.isPresent()) {
				ticketIssueCostAttachmentRepository.delete(opt.get());
				return true;
			}
		}
		return false;
	}



}
