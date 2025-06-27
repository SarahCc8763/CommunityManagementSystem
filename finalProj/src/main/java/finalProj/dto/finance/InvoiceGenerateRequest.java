package finalProj.dto.finance;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class InvoiceGenerateRequest {
    private String feeTypeName;
    private String billingPeriodCode;
    private BigDecimal unitPrice;
}
