package finalProj.service.ticket;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.ticket.Ticket;
import finalProj.domain.ticket.TicketComment;
import finalProj.dto.ticket.CommentDTO;
import finalProj.repository.ticket.TicketCommentRepository;
import finalProj.repository.ticket.TicketRepository;
import finalProj.repository.ticket.UsersRepository;

@Service
@Transactional
public class TicketCommentService {

	@Autowired
	private TicketCommentRepository ticketCommentRepository;

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private UsersRepository usersRepository;

	// 新增留言
	public TicketComment save(CommentDTO commentDTO) {
		if (commentDTO.getTicketId() == null) {
			throw new IllegalArgumentException("必須指定報修單 ID");
		}

		if (!usersRepository.existsById(commentDTO.getCommenterId())) {
			throw new IllegalArgumentException("使用者 ID 不存在：" + commentDTO.getCommenterId());
		}

		// 直接查出 Ticket 實體
		Ticket ticket = ticketRepository.findById(commentDTO.getTicketId())
				.orElseThrow(() -> new IllegalArgumentException("報修單 ID 不存在：" + commentDTO.getTicketId()));

		TicketComment comment = new TicketComment();
		comment.setTicket(ticket); // ✅ 設定 Ticket 實體
		comment.setCommenterId(commentDTO.getCommenterId());
		comment.setComment(commentDTO.getComment());

		return ticketCommentRepository.save(comment);
	}

	// 更新留言
	public TicketComment update(Integer id, CommentDTO commentDTO) {
		if (commentDTO.getTicketId() == null) {
			throw new IllegalArgumentException("必須指定報修單 ID");
		}

		if (!usersRepository.existsById(commentDTO.getCommenterId())) {
			throw new IllegalArgumentException("使用者 ID 不存在：" + commentDTO.getCommenterId());
		}

		// 查留言本體
		TicketComment comment = ticketCommentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("留言 ID 不存在：" + id));

		// 查 Ticket 實體（只查一次，省掉 existsById + findById 重複）
		Ticket ticket = ticketRepository.findById(commentDTO.getTicketId())
				.orElseThrow(() -> new IllegalArgumentException("報修單 ID 不存在：" + commentDTO.getTicketId()));

		// 設定 Ticket 實體
		comment.setTicket(ticket); // ✅ 改這裡
		comment.setCommenterId(commentDTO.getCommenterId());
		comment.setComment(commentDTO.getComment());

		return ticketCommentRepository.save(comment);
	}

	// 查詢所有留言
	public List<TicketComment> findAll() {
		return ticketCommentRepository.findAll();
	}

	// 查詢單一留言
	public Optional<TicketComment> findById(Integer id) {
		return ticketCommentRepository.findById(id);
	}

	// 查某張 ticket 的所有留言
	public List<TicketComment> findByTicketId(Integer ticketId) {
		return ticketCommentRepository.findByTicketId(ticketId);
	}

	// 刪除留言
	public boolean remove(Integer id) {
		if (id != null) {
			Optional<TicketComment> opt = ticketCommentRepository.findById(id);
			if (opt.isPresent()) {
				ticketCommentRepository.delete(opt.get());
				return true;
			}
		}
		return false;
	}

}
