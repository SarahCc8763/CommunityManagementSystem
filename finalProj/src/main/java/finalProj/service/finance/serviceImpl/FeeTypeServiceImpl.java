package finalProj.service.finance.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.finance.FeeType;
import finalProj.repository.finance.FeeTypeRepository;
import finalProj.service.finance.baseServiceInterfaces.FeeTypeService;

@Service
public class FeeTypeServiceImpl implements FeeTypeService {

    @Autowired
    private FeeTypeRepository feeTypeRepository;

    @Override
    public List<FeeType> findAll() {
        return feeTypeRepository.findAll();
    }

    @Override
    public FeeType findById(Integer id) {
        Optional<FeeType> optional = feeTypeRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public FeeType save(FeeType entity) {
        return feeTypeRepository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        feeTypeRepository.deleteById(id);
    }

    @Override
    public List<FeeType> findByCommunityId(Integer communityId) {
        return feeTypeRepository.findByCommunityId(communityId);
    }
}
