package finalProj.service.finance.baseServiceInterfaces;

import java.util.List;

import finalProj.domain.finance.Invoice;

public interface InvoiceService extends BaseService<Invoice, Integer> {
    List<Invoice> findUnpaidInvoices();

    List<Invoice> generateInvoicesForPeriod(String periodCode);

    Invoice updateInvoiceStatus(Integer id, String status);

    Invoice save(Invoice invoice);

    Invoice findById(Integer invoiceId);

    List<Invoice> findAll();

    void deleteById(Integer invoiceId);

    java.math.BigDecimal getUnitCountByUserAndFeeType(Integer usersId, Integer feeTypeId);

    // 根據communityId查詢
    java.util.List<Invoice> findByCommunityId(Integer communityId);

    // 查詢所有status為false（待審核）
    java.util.List<Invoice> findByStatus(Boolean status);

    // 根據 userId 查詢未繳帳單
    List<Invoice> findUnpaidInvoicesByUserId(Integer usersId);

    List<Invoice> findUnpaidWithResponseByCommunityId(Integer communityId);
}
