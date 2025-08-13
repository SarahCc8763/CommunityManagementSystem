package finalProj.repository.finance;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import finalProj.domain.finance.Invoice;
import finalProj.dto.finance.InvoiceDTO;

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

    // 查詢某用戶所有未繳帳單
    List<Invoice> findByUsers_UsersIdAndPaymentStatusNotIgnoreCase(Integer usersId, String status);

    // 查詢某用戶所有 paymentStatus = 'unpaid' 帳單
    List<Invoice> findByUsers_UsersIdAndPaymentStatusIgnoreCase(Integer usersId, String status);

    // 查詢某社區所有未繳帳單
    @Query("SELECT i FROM Invoice i WHERE i.communityId = :communityId AND i.paymentStatus = :paymentStatus")
    List<Invoice> findByCommunityIdAndPaymentStatus(@Param("communityId") Integer communityId,
            @Param("paymentStatus") String paymentStatus);

    // 查詢某用戶所有 paymentStatus 不是 'paid' 的帳單（即 unpaid + pending）
    @Query("SELECT i FROM Invoice i WHERE i.users.usersId = :usersId AND i.paymentStatus <> 'paid'")
    List<Invoice> findUnpaidOrPendingByUserId(@Param("usersId") Integer usersId);
}
