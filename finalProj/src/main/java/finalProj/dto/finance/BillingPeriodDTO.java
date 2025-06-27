package finalProj.dto.finance;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BillingPeriodDTO {
    private String periodName;
    private String periodCode;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime dueDate;
    private String note;

    private Integer feeTypeId;

    // BaseEntity 欄位
    private Integer communityId;
    private Boolean status;
    private String createdBy;
    private Integer updatedBy;
}
