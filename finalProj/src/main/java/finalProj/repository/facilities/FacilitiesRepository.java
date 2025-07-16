package finalProj.repository.facilities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.facilities.FacilitiesBean;

@Repository
public interface FacilitiesRepository extends JpaRepository<FacilitiesBean, Integer> {
	
	List<FacilitiesBean> findByCommunityCommunityId(Integer communityId);
	
    List<FacilitiesBean> findByCommunityCommunityIdAndFacilityStatus(Integer communityId, String facilityStatus);

    List<FacilitiesBean> findByFacilityStatus(String facilityStatus);	
	
}
