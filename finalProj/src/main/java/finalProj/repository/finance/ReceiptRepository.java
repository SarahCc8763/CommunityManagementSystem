package finalProj.repository.finance;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.finance.Receipt;

public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {

}
