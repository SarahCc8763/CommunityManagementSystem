package finalProj.service.facilities;

import java.util.List;

import finalProj.domain.facilities.FacilitiesBean;

public interface FacilitiesService {
	
	List<FacilitiesBean> findAll();                         // 查詢全部
	
    FacilitiesBean findById(Integer id);                    // 查詢單筆
    
    FacilitiesBean update(FacilitiesBean facility);         // 更新

    FacilitiesBean create(FacilitiesBean facility);         // 新增
    
    void deleteById(Integer id);                            // 刪除
    
    List<FacilitiesBean> findByCommunityId(Integer communityId);  //findByCommunityId
    
    List<FacilitiesBean> findByCommunityCommunityIdAndFacilityStatus(Integer communityId, String facilityStatus);

    List<FacilitiesBean> findByFacilityStatus(String facilityStatus);
}
