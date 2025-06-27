package finalProj.service.finance.baseServiceInterfaces;

import finalProj.domain.finance.Receipt;

public interface ReceiptService extends BaseService<Receipt, Long> {
    Receipt confirmPayment(Long invoiceId, Receipt receiptData);
}