package finalProj.repository.ticket;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domin.ticket.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

}
