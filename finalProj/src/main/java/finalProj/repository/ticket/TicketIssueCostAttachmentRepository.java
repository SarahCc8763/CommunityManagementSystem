package finalProj.repository.ticket;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.ticket.TicketIssueCostAttachment;


public interface TicketIssueCostAttachmentRepository extends JpaRepository<TicketIssueCostAttachment, Integer> {
	List<TicketIssueCostAttachment> findByTicketId(Integer ticketId);
}
