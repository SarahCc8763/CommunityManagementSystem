package finalProj.dto.finance;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

import lombok.Data;

@Data
public class InvoiceDTO {
    private Integer invoiceId;
    private BigDecimal amountDue;
    private LocalDateTime deadline;
    private String paymentPlan;
    private BigDecimal unitCount;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private String paymentStatus;
    private String note;
    private String periodName;

    private UserSimpleDTO user;
    private FeeTypeDTO feeType;
    private BillingPeriodDTO billingPeriod;
    private ReceiptDTO receipt;

    private Integer communityId;
    private Integer createdBy;
    private Integer updatedBy;

    private List<InvoiceResponseDTO> invoiceResponses;

    public InvoiceDTO(finalProj.domain.finance.Invoice i) {
        this.invoiceId = i.getInvoiceId();
        this.amountDue = i.getAmountDue();
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
            this.periodName = i.getBillingPeriod().getPeriodName(); // ✅ 正確來源
        }

        if (i.getInvoiceResponses() != null) {
            this.invoiceResponses = i.getInvoiceResponses().stream()
                    .map(InvoiceResponseDTO::new)
                    .collect(Collectors.toList());
        } else {
            this.invoiceResponses = new ArrayList<>();
        }

        System.out.println("InvoiceDTO from invoiceId = " + i.getInvoiceId() + ", status = " + i.getPaymentStatus());
        System.out.println("invoiceId=" + i.getInvoiceId()
                + ", periodName=" + this.periodName
                + ", billingPeriod=" + (this.billingPeriod != null ? this.billingPeriod.getPeriodName() : "null"));
    }

    public InvoiceDTO() {
    }
}
