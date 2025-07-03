package finalProj.repository.finance;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.finance.InvoiceResponse;
import java.util.List;

public interface InvoiceResponseRepository extends JpaRepository<InvoiceResponse, Integer> {

    List<InvoiceResponse> findByInvoiceId(Integer invoiceId);

}
