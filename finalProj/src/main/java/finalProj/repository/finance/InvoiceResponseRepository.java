package finalProj.repository.finance;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.finance.InvoiceResponse;

public interface InvoiceResponseRepository extends JpaRepository<InvoiceResponse, Integer> {

}
