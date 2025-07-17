package finalProj.service.finance.baseServiceInterfaces;

import java.util.List;

public interface InvoiceGeneratingService {

    Boolean generateInvoices(Integer feeTypeId, Integer billingPeriodId, Integer createdBy);

    // 新增：批次產生請款單
    void batchGenerateInvoices(Integer billingPeriodId, Integer feeTypeId, List<Integer> userIds, Integer createdBy);
}