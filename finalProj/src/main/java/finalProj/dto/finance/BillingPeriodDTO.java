package finalProj.dto.finance;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BillingPeriodDTO {
    private Integer billingPeriodId;
    private String periodName;
    private String periodCode;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime dueDate;
    private String note;
    private FeeTypeDTO feeType;
    private Integer communityId;
    private Boolean status;
    private Integer feeTypeId;

    // BaseEntity 欄位
    private String createdBy;
    private Integer updatedBy;

    public BillingPeriodDTO(finalProj.domain.finance.BillingPeriod entity) {
        this.billingPeriodId = entity.getBillingPeriodId();
        this.periodName = entity.getPeriodName();
        this.periodCode = entity.getPeriodCode();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.dueDate = entity.getDueDate();
        this.note = entity.getNote();
        this.communityId = entity.getCommunityId();
        this.status = entity.getStatus();

    }

    public BillingPeriodDTO() {
    }
}
