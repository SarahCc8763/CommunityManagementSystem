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

    // 建構子：可接受 Invoice Entity 自動轉換
    public InvoiceDTO(finalProj.domain.finance.Invoice i) {
        this.invoiceId = i.getInvoiceId();
        this.amountDue = i.getAmountDue();
        this.periodName = i.getPeriodName();
        this.deadline = i.getDeadline();
        this.paymentPlan = i.getPaymentPlan();
        this.unitCount = i.getUnitCount();
        this.unitPrice = i.getUnitPrice();
        this.totalAmount = i.getTotalAmount();
        this.paymentStatus = i.getPaymentStatus();
        this.note = i.getNote();
        this.communityId = i.getCommunityId();
        this.createdBy = i.getCreatedBy();
        this.updatedBy = i.getUpdatedBy();

        if (i.getUsers() != null) {
            this.user = new UserSimpleDTO(i.getUsers());
        }
        if (i.getFeeType() != null) {
            this.feeType = new FeeTypeDTO(i.getFeeType());
        }
        if (i.getBillingPeriod() != null) {
            this.billingPeriod = new BillingPeriodDTO(i.getBillingPeriod());
        }
    }

    public InvoiceDTO() {
    }

    public List<InvoiceResponseDTO> getInvoiceResponses() {
        return invoiceResponses;
    }

    public void setInvoiceResponses(List<InvoiceResponseDTO> invoiceResponses) {
        this.invoiceResponses = invoiceResponses;
    }

    private List<InvoiceResponseDTO> invoiceResponses;
}
