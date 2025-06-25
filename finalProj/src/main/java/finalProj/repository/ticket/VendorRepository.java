package finalProj.repository.ticket;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domin.ticket.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {

}
