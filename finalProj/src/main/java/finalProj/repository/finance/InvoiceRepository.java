package finalProj.repository.finance;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.finance.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    // 根據 periodCode 查詢該期所有請款單
    List<Invoice> findByBillingPeriod_PeriodCode(String periodCode);

    // 根據 invoiceId 查詢
    Optional<Invoice> findByInvoiceId(Integer invoiceId);
}
