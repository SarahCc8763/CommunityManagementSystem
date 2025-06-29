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

    // BaseEntity 欄位
    private String createdBy;
    private Integer updatedBy;
}
