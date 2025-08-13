package finalProj.service.facilities;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.facilities.FacilitiesBean;
import finalProj.exception.facilities.ResourceNotFoundException;
import finalProj.repository.facilities.FacilitiesRepository;

@Service
public class FacilitiesServiceImpl implements FacilitiesService {

	@Autowired
	private FacilitiesRepository facilitiesRepository;

	@Override
	public List<FacilitiesBean> findAll() {
		return facilitiesRepository.findAll();
	}

	@Override
	public FacilitiesBean findById(Integer id) {
		// 使用 orElseThrow() 來拋出異常
		return facilitiesRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("找不到 ID 為 " + id + " 的設施。"));
	}

	@Override
	public FacilitiesBean update(FacilitiesBean facility) {
		if (facility.getFacilityId() == null || !facilitiesRepository.existsById(facility.getFacilityId())) {
			throw new ResourceNotFoundException("無法更新，找不到公設 ID: " + facility.getFacilityId());
		}
		facility.setUpdatedAt(LocalDateTime.now());
		return facilitiesRepository.save(facility);
	}

	@Override
	public FacilitiesBean create(FacilitiesBean facility) {
		if (facility.getFacilityId() != null) {
			throw new IllegalArgumentException("新增時不應包含 facilityId，請勿使用已存在的 ID");
		}
		facility.setFacilityStatus("active");
		facility.setCreatedAt(LocalDateTime.now());
		return facilitiesRepository.save(facility);
	}

	@Override
	public void deleteById(Integer id) {
		if (!facilitiesRepository.existsById(id)) {
			throw new ResourceNotFoundException("刪除失敗，找不到 ID: " + id);
		}
		facilitiesRepository.deleteById(id);
	}
	
	@Override
	public List<FacilitiesBean> findByCommunityId(Integer communityId) {
	    return facilitiesRepository.findByCommunityCommunityId(communityId);
	}

	@Override
	public List<FacilitiesBean> findByCommunityCommunityIdAndFacilityStatus(Integer communityId, String status) {		
		return facilitiesRepository.findByCommunityCommunityIdAndFacilityStatus(communityId, status);
	}

	@Override
	public List<FacilitiesBean> findByFacilityStatus(String status) {
		return facilitiesRepository.findByFacilityStatus(status);
	}
}
