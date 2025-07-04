package finalProj.repository.finance;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.finance.Receipt;
import java.util.List;

public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {

    // 根據communityId查詢
    List<Receipt> findByCommunityId(Integer communityId);

}
