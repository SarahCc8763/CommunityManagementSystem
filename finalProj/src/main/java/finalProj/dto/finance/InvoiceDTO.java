package finalProj.dto.finance;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    private String paymentStatus;
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

    public List<InvoiceResponseDTO> getInvoiceResponses() {
        return invoiceResponses;
    }

    public void setInvoiceResponses(List<InvoiceResponseDTO> invoiceResponses) {
        this.invoiceResponses = invoiceResponses;
    }

    private List<InvoiceResponseDTO> invoiceResponses;
}
