package finalProj.dto.finance;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class FeeTypeDTO {
    private Integer feeTypeId;
    private String description;
    private String feeCode;
    private BigDecimal amountPerUnit;
    private String frequency;
    private String unit;
    private String note;

    // BaseEntity fields
    private Integer communityId;
    private Boolean status;
    private Integer createdBy;
    private Integer updatedBy;
}
