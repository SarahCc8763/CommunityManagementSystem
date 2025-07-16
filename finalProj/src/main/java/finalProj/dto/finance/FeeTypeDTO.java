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

    public FeeTypeDTO(finalProj.domain.finance.FeeType entity) {
        this.feeTypeId = entity.getFeeTypeId();
        this.description = entity.getDescription();
        this.feeCode = entity.getFeeCode();
        this.amountPerUnit = entity.getAmountPerUnit();
        this.frequency = entity.getFrequency();
        this.unit = entity.getUnit();
        this.note = entity.getNote();
        this.communityId = entity.getCommunityId();
        this.status = entity.getStatus();
        this.createdBy = entity.getCreatedBy();
        this.updatedBy = entity.getUpdatedBy();
    }

    public FeeTypeDTO() {
    }
}
