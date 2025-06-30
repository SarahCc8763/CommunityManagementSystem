package finalProj.service.finance.baseServiceInterfaces;

import java.util.List;

import finalProj.domain.finance.Invoice;

public interface InvoiceService extends BaseService<Invoice, Integer> {

    List<Invoice> generateInvoicesForPeriod(String periodCode);

    Invoice updateInvoiceStatus(Integer id, Boolean status);

    Invoice save(Invoice invoice);

    Invoice findById(Integer invoiceId);

    List<Invoice> findAll();

    void deleteById(Integer invoiceId);

    java.math.BigDecimal getUnitCountByUserAndFeeType(Integer usersId, Integer feeTypeId);
}
