package finalProj.service.ticket;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domin.community.Community;
import finalProj.domin.ticket.Ticket;
import finalProj.dto.ticket.TicketDTO;
import finalProj.repository.community.CommunityRepository;
import finalProj.repository.ticket.TicketAttachmentRepository;
import finalProj.repository.ticket.TicketRepository;
import finalProj.repository.ticket.UsersRepository;

@Service
@Transactional
public class TicketService {

	@Autowired
	private TicketRepository ticketRepository;
	@Autowired
	private CommunityRepository communityRepository;
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private TicketAttachmentRepository ticketAttachmentRepository;

	// 查詢一筆資料
	public Ticket findById(Integer id) {
		if (id != null) {
			Optional<Ticket> optional = ticketRepository.findById(id);
			if (optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}

	// 新增ticket
	public Ticket save(TicketDTO dto) {
		if (dto.getCommunityId() == null) {
			throw new IllegalArgumentException("必須指定社區 ID");
		}

		if (!usersRepository.existsById(dto.getReporterId())) {
			throw new IllegalArgumentException("使用者 ID 不存在：" + dto.getReporterId());
		}

		Community community = communityRepository.findById(dto.getCommunityId())
				.orElseThrow(() -> new IllegalArgumentException("社區 ID 不存在：" + dto.getCommunityId()));

		Ticket ticket = new Ticket();
		ticket.setCommunity(community);
		ticket.setReporterId(dto.getReporterId());
		ticket.setTitle(dto.getTitle());
		ticket.setAssignerId(dto.getAssignerId());
		ticket.setStatus(dto.getStatus());
		ticket.setIssueDescription(dto.getIssueDescription());
		ticket.setNotes(dto.getNotes());


		// 處理附件
		Ticket saved = ticketRepository.save(ticket);
		if (dto.getAttachmentIds() != null) {
			for (Integer attachmentId : dto.getAttachmentIds()) {
				ticketAttachmentRepository.findById(attachmentId).ifPresent(attachment -> {
					attachment.setTicket(saved);
					ticketAttachmentRepository.save(attachment);
				});
			}
		}

		return saved;
	}

	// ticket id 是否存在
	public boolean exists(Integer id) {
		if (id != null) {
			return ticketRepository.existsById(id);
		}
		return false;
	}

	// 刪除一筆資料
	public boolean remove(Integer id) {
		if (id != null) {
			if (ticketRepository.existsById(id)) {
				ticketRepository.deleteById(id);
				return true;
			}
		}
		return false;
	}

	// 修改ticket
	public Ticket update(Integer id, TicketDTO dto) {

		if (!usersRepository.existsById(dto.getReporterId())) {
			throw new IllegalArgumentException("報修人 ID 不存在：" + dto.getReporterId());
		}

		if (dto.getAssignerId() != null && !usersRepository.existsById(dto.getAssignerId())) {
			throw new IllegalArgumentException("指派人 ID 不存在：" + dto.getAssignerId());
		}

		Community community = communityRepository.findById(dto.getCommunityId())
				.orElseThrow(() -> new IllegalArgumentException("社區 ID 不存在：" + dto.getCommunityId()));

		Ticket ticket = ticketRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Ticket ID 不存在：" + id));
		ticket.setId(id);
		ticket.setCommunity(community);
		ticket.setReporterId(dto.getReporterId());
		ticket.setAssignerId(dto.getAssignerId());
		ticket.setTitle(dto.getTitle());
		ticket.setStatus(dto.getStatus());
		ticket.setIssueDescription(dto.getIssueDescription());
		ticket.setNotes(dto.getNotes());
		ticket.setActionBy(dto.getActionBy());
		ticket.setActionTime(new Date());

		return ticketRepository.save(ticket);
	}

	// 找尋所有資料
	public List<Ticket> findAll() {
		return ticketRepository.findAll();
	}

}
