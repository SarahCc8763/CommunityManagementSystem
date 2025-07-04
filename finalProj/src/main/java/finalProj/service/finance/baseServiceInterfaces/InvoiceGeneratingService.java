package finalProj.service.finance.baseServiceInterfaces;

import java.math.BigDecimal;

public interface InvoiceGeneratingService {

    void generateInvoices(Integer feeTypeId, Integer billingPeriodId);

}