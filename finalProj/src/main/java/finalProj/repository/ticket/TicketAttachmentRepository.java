package finalProj.repository.ticket;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domin.ticket.TicketAttachment;


public interface TicketAttachmentRepository extends JpaRepository<TicketAttachment , Integer> {
	
    List<TicketAttachment> findByTicketId(Integer ticketId);
    List<TicketAttachment> findByComment_Id(Integer commentId);
    
}
