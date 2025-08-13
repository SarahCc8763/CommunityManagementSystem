package finalProj.service.facilities;

import java.util.List;

import finalProj.domain.facilities.FacilityImagesBean;

public interface FacilityImagesService {

	List<FacilityImagesBean> findAll(); // 查全部圖片

	List<FacilityImagesBean> findByFacilityId(Integer facilityId); // 查某個公設的所有圖片

	FacilityImagesBean findById(Integer id); // 查單筆圖片

	FacilityImagesBean save(FacilityImagesBean image); // 新增或更新圖片

	void deleteById(Integer id); // 刪除圖片

}
