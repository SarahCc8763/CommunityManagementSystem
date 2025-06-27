package finalProj.repository.finance;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.finance.BillingPeriod;

@Repository
public interface BillingPeriodRepository extends JpaRepository<BillingPeriod, Integer> {
    Optional<BillingPeriod> findByPeriodCode(String periodCode);

    Optional<BillingPeriod> findByPeriodName(String periodName);
}
