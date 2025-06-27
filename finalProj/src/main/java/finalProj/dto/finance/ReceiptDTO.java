package finalProj.dto.finance;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReceiptDTO {
    private Integer invoiceId;
    private String paymentMethod;
    private LocalDateTime paidAt;
    private LocalDateTime debitAt;
    private Integer amountPay;
    private String installments;

}