package finalProj.service.finance.baseServiceInterfaces;

import finalProj.domain.finance.BillingPeriod;

public interface BillingPeriodService extends BaseService<BillingPeriod, Integer> {
    BillingPeriod createPeriodIfNotExists(String periodCode);

    BillingPeriod findByPeriodCode(String periodCode);

    BillingPeriod save(BillingPeriod period);

    BillingPeriod findByPeriodName(String periodName);

    // 根據communityId查詢
    java.util.List<BillingPeriod> findByCommunityId(Integer communityId);
}
