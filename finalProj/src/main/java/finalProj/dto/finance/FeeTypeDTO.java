package finalProj.dto.finance;

import lombok.Data;

@Data
public class FeeTypeDTO {
    private String description;
    private String note;

    // BaseEntity fields
    private Integer communityId;
    private Boolean status;
    private Integer createdBy;
    private Integer updatedBy;
}
