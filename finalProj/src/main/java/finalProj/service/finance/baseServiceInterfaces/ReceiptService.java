package finalProj.service.finance.baseServiceInterfaces;

import finalProj.domain.finance.Receipt;
import finalProj.dto.finance.ReceiptDTO;

public interface ReceiptService extends BaseService<Receipt, Integer> {
    Receipt createReceipt(ReceiptDTO dto);
}