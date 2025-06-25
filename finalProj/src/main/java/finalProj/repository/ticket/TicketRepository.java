package finalProj.repository.ticket;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.ticket.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

	
	
	
}
