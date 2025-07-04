package finalProj.service.finance.baseServiceInterfaces;

public interface InvoiceGeneratingService {

    void generateInvoices(Integer feeTypeId, Integer billingPeriodId);

}