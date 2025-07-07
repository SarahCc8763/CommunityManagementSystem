package finalProj.service.finance.baseServiceInterfaces;

import java.util.List;

import finalProj.domain.finance.Invoice;
import finalProj.dto.finance.InvoiceDTO;

public interface InvoiceService extends BaseService<Invoice, Integer> {

    List<Invoice> generateInvoicesForPeriod(String periodCode);

    Invoice updateInvoiceStatus(Integer id, String status);

    Invoice save(Invoice invoice);

    Invoice findById(Integer invoiceId);

    Invoice findByStatus(Boolean status);

    List<Invoice> findAll();

    void deleteById(Integer invoiceId);

    java.math.BigDecimal getUnitCountByUserAndFeeType(Integer usersId, Integer feeTypeId);

    // -----------------------------------------
    List<InvoiceDTO> findUnpaidInvoicesWithResponse();

    List<InvoiceDTO> findUnpaidInvoicesByUserId(Integer userId);

    List<InvoiceDTO> findUnpaidInvoicesByCommunityId(Integer communityId);

}
