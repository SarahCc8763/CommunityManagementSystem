package finalProj.service.finance.baseServiceInterfaces;

import java.math.BigDecimal;

public interface InvoiceGeneratingService {
    void generateInvoices(String feeTypeName, String billingPeriodCode, BigDecimal unitPrice);
}