package finalProj.dto.finance;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class InvoiceResponseDTO {
    private Integer invoiceResponseId;
    private Integer invoiceId;
    private Integer userId;
    private String accountCode; // 帳號末五碼（非必填）
    private String lastResponse; // 備註
    private LocalDateTime lastResponseTime;
    private Boolean verified; // 管理員是否已審核
    private LocalDateTime verifiedTime; // 審核時間
    private Integer verifiedBy; // 審核人員ID
}