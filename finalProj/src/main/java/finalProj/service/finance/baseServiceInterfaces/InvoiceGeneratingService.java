package finalProj.service.finance.baseServiceInterfaces;

public interface InvoiceGeneratingService {

    Boolean generateInvoices(Integer feeTypeId, Integer billingPeriodId, Integer createdBy);

}