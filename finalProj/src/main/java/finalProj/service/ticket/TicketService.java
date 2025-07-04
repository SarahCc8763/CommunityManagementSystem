package finalProj.service.ticket;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.community.Community;
import finalProj.domain.ticket.IssueType;
import finalProj.domain.ticket.IssueTypeAndTicket;
import finalProj.domain.ticket.Ticket;
import finalProj.domain.users.Users;
import finalProj.dto.ticket.TicketDTO;
import finalProj.dto.ticket.TicketSearchDTO;
import finalProj.enumCommunity.CommunityFunction;
import finalProj.repository.community.CommunityRepository;
import finalProj.repository.ticket.IssueTypeAndTicketRepository;
import finalProj.repository.ticket.IssueTypeRepository;
import finalProj.repository.ticket.TicketAttachmentRepository;
import finalProj.repository.ticket.TicketRepository;
import finalProj.repository.users.UsersRepository;
import finalProj.util.CommunityFunctionUtils;

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
	@Autowired
	private IssueTypeRepository issueTypeRepository;
	@Autowired
	private IssueTypeAndTicketRepository issueTypeAndTicketRepository;

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

		Community community = communityRepository.findById(dto.getCommunityId())
				.orElseThrow(() -> new IllegalArgumentException("社區 ID 不存在：" + dto.getCommunityId()));

		if (!CommunityFunctionUtils.hasFunction(community.getFunction(), CommunityFunction.TICKET)) {
			throw new IllegalArgumentException("此社區未啟用『報修』功能，無法建立報修單");
		}

		if (dto.getCommunityId() == null) {
			throw new IllegalArgumentException("必須指定社區 ID");
		}

		if (!usersRepository.existsById(dto.getReporterId())) {
			throw new IllegalArgumentException("使用者 ID 不存在：" + dto.getReporterId());
		}

		Users reporter = usersRepository.findById(dto.getReporterId())
				.orElseThrow(() -> new IllegalArgumentException("使用者 ID 不存在：" + dto.getReporterId()));

		Users assigner = null;
		if (dto.getAssignerId() != null) {
			assigner = usersRepository.findById(dto.getAssignerId())
					.orElseThrow(() -> new IllegalArgumentException("管理者 ID 不存在：" + dto.getAssignerId()));
		}

		Ticket ticket = new Ticket();
		ticket.setCommunity(community);
		ticket.setReporter(reporter);
		ticket.setTitle(dto.getTitle());
		ticket.setAssignerId(assigner);
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
		if (dto.getIssueTypeNames() != null) {
		    for (String name : dto.getIssueTypeNames()) {
		        IssueType issueType = issueTypeRepository.findByIssueTypeName(name)
		                .orElseThrow(() -> new IllegalArgumentException("找不到對應的 issueType: " + name));

		        IssueTypeAndTicket rel = new IssueTypeAndTicket();
		        rel.setTicket(saved);
		        rel.setIssueType(issueType);

		        issueTypeAndTicketRepository.save(rel);
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

		Community community = communityRepository.findById(dto.getCommunityId())
				.orElseThrow(() -> new IllegalArgumentException("社區 ID 不存在：" + dto.getCommunityId()));

		if (!CommunityFunctionUtils.hasFunction(community.getFunction(), CommunityFunction.TICKET)) {
			throw new IllegalArgumentException("此社區未啟用此功能");
		}

		if (!usersRepository.existsById(dto.getReporterId())) {
			throw new IllegalArgumentException("報修人 ID 不存在：" + dto.getReporterId());
		}

		if (dto.getAssignerId() != null && !usersRepository.existsById(dto.getAssignerId())) {
			throw new IllegalArgumentException("指派人 ID 不存在：" + dto.getAssignerId());
		}

		Ticket ticket = ticketRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Ticket ID 不存在：" + id));

		Users reporter = usersRepository.findById(dto.getReporterId())
				.orElseThrow(() -> new IllegalArgumentException("使用者 ID 不存在：" + dto.getReporterId()));

		Users assigner = usersRepository.findById(dto.getAssignerId())
				.orElseThrow(() -> new IllegalArgumentException("管理者 ID 不存在：" + dto.getReporterId()));

		ticket.setId(id);
		ticket.setCommunity(community);
		ticket.setReporter(reporter);
		ticket.setAssignerId(assigner);
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

	// 找尋特定資料
	public List<Ticket> searchTickets(TicketSearchDTO dto) {
	    List<String> issueTypes = dto.getIssueTypeNames() != null ? dto.getIssueTypeNames() : new ArrayList<>();
	    int issueTypeSize = issueTypes.size();

	    return ticketRepository.searchTickets(
	        dto.getTitle(),
	        dto.getStatus(),
	        dto.getStartDate(),
	        dto.getReporterId(),
	        issueTypes,
	        issueTypeSize
	    );
	}

}
