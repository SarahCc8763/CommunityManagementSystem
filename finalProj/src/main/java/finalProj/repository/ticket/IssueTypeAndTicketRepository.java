package finalProj.repository.ticket;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.ticket.IssueTypeAndTicket;

public interface IssueTypeAndTicketRepository extends JpaRepository<IssueTypeAndTicket, Integer> {

	List<IssueTypeAndTicket> findByTicketId(Integer ticketId);

	List<IssueTypeAndTicket> findByIssueTypeId(Integer issueTypeId);

}
