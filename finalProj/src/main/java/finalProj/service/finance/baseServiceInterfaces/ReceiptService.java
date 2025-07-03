package finalProj.service.finance.baseServiceInterfaces;

import finalProj.domain.finance.Receipt;
import finalProj.dto.finance.ReceiptDTO;

public interface ReceiptService extends BaseService<Receipt, Integer> {
    ReceiptDTO createReceipt(ReceiptDTO dto);

    // 根據communityId查詢
    java.util.List<Receipt> findByCommunityId(Integer communityId);
}