package finalProj.dto.finance;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class InvoiceDTO {
    private Integer invoiceId;
    private BigDecimal amountDue;
    private String periodName;
    private LocalDateTime deadline;
    private String paymentPlan;
    private BigDecimal unitCount;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private Boolean status;
    private String note;

    // 巢狀物件
    private UserSimpleDTO user;
    private FeeTypeDTO feeType;
    private BillingPeriodDTO billingPeriod;
    private ReceiptDTO receipt;

    // BaseEntity
    private Integer communityId;
    private Integer createdBy;
    private Integer updatedBy;
}
