package finalProj.service.finance.baseServiceInterfaces;

//import finalProj.service.finance.baseServiceInterfaces.BaseService;
import finalProj.domain.finance.FeeType;

public interface FeeTypeService extends BaseService<FeeType, Integer> {
    // 新增：根據communityId查詢
    java.util.List<FeeType> findByCommunityId(Integer communityId);
}
