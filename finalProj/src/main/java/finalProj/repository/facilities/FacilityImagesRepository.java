package finalProj.repository.facilities;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.facilities.FacilityImagesBean;

@Repository
public interface FacilityImagesRepository extends JpaRepository<FacilityImagesBean, Integer> {
	 
	List<FacilityImagesBean> findByFacilityFacilityId(Integer facilityId);
	
	Optional<FacilityImagesBean> findFirstByFacilityFacilityIdOrderBySortOrderAsc(Integer facilityId);
	
}
