package finalProj.repository.ticket;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.ticket.TicketComment;

public interface TicketCommentRepository extends JpaRepository<TicketComment, Integer> {

	List<TicketComment> findByTicketId(Integer ticketId);

}
