package finalProj.service.finance.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.finance.BillingPeriod;
import finalProj.repository.finance.BillingPeriodRepository;

import finalProj.service.finance.baseServiceInterfaces.BillingPeriodService;

@Service
public class BillingPeriodServiceImpl implements BillingPeriodService {

    @Autowired
    private BillingPeriodRepository billingPeriodRepository;

    @Override
    public List<BillingPeriod> findAll() {
        return billingPeriodRepository.findAll();
    }

    @Override
    public BillingPeriod findByPeriodName(String periodName) {
        return billingPeriodRepository.findByPeriodName(periodName).orElse(null);
    }

    @Override
    public BillingPeriod findById(Integer id) {
        Optional<BillingPeriod> optional = billingPeriodRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public BillingPeriod save(BillingPeriod billingPeriod) {
        return billingPeriodRepository.save(billingPeriod);
    }

    @Override
    public void deleteById(Integer id) {
        billingPeriodRepository.deleteById(id);
    }

    @Override
    public BillingPeriod findByPeriodCode(String periodCode) {
        return billingPeriodRepository.findByPeriodCode(periodCode).orElse(null);
    }

    @Override
    public BillingPeriod createPeriodIfNotExists(String periodCode) {
        Optional<BillingPeriod> optional = billingPeriodRepository.findByPeriodCode(periodCode);
        if (optional.isPresent()) {
            return optional.get();
        }

        BillingPeriod newPeriod = new BillingPeriod();
        newPeriod.setPeriodCode(periodCode);
        return billingPeriodRepository.save(newPeriod);
    }

    @Override
    public List<BillingPeriod> findByCommunityId(Integer communityId) {
        return billingPeriodRepository.findByCommunityId(communityId);
    }
}
