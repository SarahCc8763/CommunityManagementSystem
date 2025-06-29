package finalProj.dto.finance;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReceiptDTO {
    private Integer receiptId;
    private Integer invoiceId;
    private String paymentMethod;
    private LocalDateTime paidAt;
    private LocalDateTime debitAt;
    private BigDecimal amountPay;
    private String installments;
    private String note;
}