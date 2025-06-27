package finalProj.dto.finance;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class InvoiceDTO {
    private Integer invoiceId;
    private Integer amountDue;
    private String periodName;
    private LocalDateTime deadline;
    private String paymentPlan;

    // BaseEntity
    private Integer communityId;
    private Boolean status;
    private Integer createdBy;
    private Integer updatedBy;
    private String note;

}
