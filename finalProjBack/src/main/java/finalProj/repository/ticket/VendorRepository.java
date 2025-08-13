package finalProj.repository.ticket;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.ticket.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {

}
