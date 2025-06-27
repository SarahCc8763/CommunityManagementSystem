package finalProj.repository.finance;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.finance.FeeType;

import java.util.Optional;

public interface FeeTypeRepository extends JpaRepository<FeeType, Integer> {
    Optional<FeeType> findByFeeCode(String feeCode);

    Optional<FeeType> findByDescription(String description);
}
