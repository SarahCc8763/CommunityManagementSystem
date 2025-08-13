package finalProj.dto.finance;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
import finalProj.dto.finance.InvoiceDTO;

@Data
@NoArgsConstructor
public class ReceiptDTO {
    private Integer receiptId;
    private Integer invoiceId;
    private String paymentMethod;
    private LocalDateTime paidAt;
    private LocalDateTime debitAt;
    private BigDecimal amountPay;
    private String installments;
    private String note;
    private Integer communityId;
    private Integer createdBy;
    private LocalDateTime createdAt;

    private Boolean status;

    // 新增：關聯發票資訊
    private InvoiceDTO invoice;
}