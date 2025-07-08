package finalProj.repository.finance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import finalProj.domain.finance.InvoiceResponse;
import finalProj.dto.finance.InvoiceDTO;

import java.util.List;

public interface InvoiceResponseRepository extends JpaRepository<InvoiceResponse, Integer> {

    List<InvoiceResponse> findByInvoice_InvoiceId(Integer invoiceId);

    @Query("""
                SELECT DISTINCT new finalProj.dto.finance.InvoiceDTO(i)
                FROM Invoice i
                JOIN InvoiceResponse r ON r.invoice.invoiceId = i.invoiceId
                WHERE i.paymentStatus = 'unpaid'
            """)
    List<InvoiceDTO> findUnpaidInvoiceWithResponse();

    @Query("""
                SELECT DISTINCT new finalProj.dto.finance.InvoiceDTO(i)
                FROM Invoice i
                JOIN InvoiceResponse r ON r.invoice.invoiceId = i.invoiceId
                WHERE i.communityId = :communityId
                  AND i.paymentStatus = 'unpaid'
            """)
    List<InvoiceDTO> findUnpaidInvoiceWithResponseByCommunityId(@Param("communityId") Integer communityId);

}
