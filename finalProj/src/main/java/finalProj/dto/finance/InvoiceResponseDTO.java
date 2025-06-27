package finalProj.dto.finance;

import lombok.Data;

@Data
public class InvoiceResponseDTO {
    private Integer invoiceId;
    private String accountCode; // 帳號末五碼（非必填）
    private String lastResponse; // 備註
}