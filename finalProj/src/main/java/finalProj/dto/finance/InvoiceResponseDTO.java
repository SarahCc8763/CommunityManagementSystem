package finalProj.dto.finance;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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

    // Entity 轉 DTO 建構子
    public InvoiceResponseDTO(finalProj.domain.finance.InvoiceResponse entity) {
        if (entity == null)
            return;
        this.invoiceResponseId = entity.getInvoiceResponseId();
        this.invoiceId = entity.getInvoice() != null ? entity.getInvoice().getInvoiceId() : null;
        this.userId = entity.getUser() != null ? entity.getUser().getUsersId() : null;
        this.accountCode = entity.getAccountCode();
        this.lastResponse = entity.getLastResponse();
        this.lastResponseTime = entity.getLastResponseTime();
        this.verified = entity.getVerified();
        this.verifiedTime = entity.getVerifiedTime();
        this.verifiedBy = entity.getVerifiedBy();
    }
}