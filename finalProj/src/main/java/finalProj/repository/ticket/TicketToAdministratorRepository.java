package finalProj.repository.ticket;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.ticket.TicketToAdministrator;

public interface TicketToAdministratorRepository extends JpaRepository<TicketToAdministrator, Integer> {

	List<TicketToAdministrator> findByTicketId(Integer ticketId);

	List<TicketToAdministrator> findByVendorId(Integer vendorId);

	void deleteByTicketId(Integer ticketId);
}
