package finalProj.repository.ticket;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import finalProj.domain.ticket.IssueTypeAndTicket;
import jakarta.transaction.Transactional;


public interface IssueTypeAndTicketRepository extends JpaRepository<IssueTypeAndTicket, Integer> {
	
	List<IssueTypeAndTicket> findByTicketId(Integer ticketId);
	List<IssueTypeAndTicket> findByIssueTypeId(Integer issueTypeId);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM IssueTypeAndTicket i WHERE i.ticket.id = :ticketId")
	void deleteByTicketId(@Param("ticketId") Integer ticketId);
    
}