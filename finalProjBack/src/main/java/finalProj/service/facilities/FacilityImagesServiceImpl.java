package finalProj.service.facilities;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.facilities.FacilityImagesBean;
import finalProj.exception.facilities.ResourceNotFoundException;
import finalProj.repository.facilities.FacilityImagesRepository;

@Service
public class FacilityImagesServiceImpl implements FacilityImagesService {

	@Autowired
	private FacilityImagesRepository facilityImagesRepository;

	@Override
	public List<FacilityImagesBean> findAll() {
		List<FacilityImagesBean> images = facilityImagesRepository.findAll();
		if (images.isEmpty()) {
			throw new ResourceNotFoundException("目前尚無任何設施圖片。");
		}
		return images;
	}

	@Override
	public List<FacilityImagesBean> findByFacilityId(Integer facilityId) {
		List<FacilityImagesBean> images = facilityImagesRepository.findByFacilityFacilityId(facilityId);
		if (images.isEmpty()) {
			throw new ResourceNotFoundException("找不到 facilityId 為 " + facilityId + " 的設施圖片。");
		}
		return images;
	}

	@Override
	public FacilityImagesBean findById(Integer id) {
		return facilityImagesRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("找不到 imageId 為 " + id + " 的設施圖片。"));
	}

	@Override
	public FacilityImagesBean save(FacilityImagesBean image) {
		return facilityImagesRepository.save(image);
	}

	@Override
	public void deleteById(Integer id) {
		if (!facilityImagesRepository.existsById(id)) {
			throw new ResourceNotFoundException("無法刪除，找不到 imageId 為 " + id + " 的設施圖片。");
		}
		facilityImagesRepository.deleteById(id);
	}
}
