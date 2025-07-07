package finalProj.repository.finance;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import finalProj.domain.finance.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    // 根據 periodCode 查詢該期所有請款單
    List<Invoice> findByBillingPeriod_PeriodCode(String periodCode);

    // 根據 invoiceId 查詢
    Optional<Invoice> findByInvoiceId(Integer invoiceId);

    // 根據communityId查詢
    List<Invoice> findByCommunityId(Integer communityId);

    // 查未繳帳單 不管大小寫
    List<Invoice> findByPaymentStatusNotIgnoreCase(String status);

    // 查詢所有status為false（待審核）
    List<Invoice> findByStatus(Boolean status);

    List<Invoice> findByPaymentStatusAndInvoiceResponsesIsNotEmptyIgnoreCase(String paymentStatus);

    // 用戶查看自己的未繳帳單
    List<Invoice> findByUsers_UsersIdAndPaymentStatusIgnoreCase(Integer usersId, String paymentStatus);

    // 管理員看該社區所有人的未繳帳單
    List<Invoice> findByCommunityIdAndPaymentStatusIgnoreCase(Integer communityId, String paymentStatus);

    @Query("SELECT i FROM Invoice i WHERE i.paymentStatus = :status AND SIZE(i.invoiceResponses) > 0")
    List<Invoice> findUnpaidInvoicesWithResponses(@Param("status") String status);

}
