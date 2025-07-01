package finalProj.service.packages;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.community.Community;
import finalProj.domain.packages.Packages;
import finalProj.domain.users.Units;
import finalProj.dto.packages.PackagesDTO;
import finalProj.dto.packages.PackagesSearchDTO;
import finalProj.enumCommunity.CommunityFunction;
import finalProj.repository.community.CommunityRepository;
import finalProj.repository.packages.PackagesRepositroy;
import finalProj.repository.users.UnitsRepository;
import finalProj.util.CommunityFunctionUtils;

@Service
@Transactional
public class PackagesService {

	@Autowired
	private PackagesRepositroy packagesRepositroy;

	@Autowired
	private CommunityRepository communityRepository;

	@Autowired
	private UnitsRepository unitsRepository;

	// 新增包裹
	public Packages addPackages(PackagesDTO dto) {
		Community community = communityRepository.findById(dto.getCommunityId())
				.orElseThrow(() -> new IllegalArgumentException("社區 ID 不存在：" + dto.getCommunityId()));

		if (!CommunityFunctionUtils.hasFunction(community.getFunction(), CommunityFunction.PACKAGE)) {
			throw new IllegalArgumentException("此社區未啟用『包裹』功能，無法建立包裹紀錄");
		}

		Units unit = unitsRepository.findById(dto.getUnitId())
				.orElseThrow(() -> new IllegalArgumentException("單位 ID 不存在：" + dto.getUnitId()));

		Packages packages = new Packages();
		packages.setCommunity(community);
		packages.setUnit(unit);
		packages.setPiece(dto.getPiece());
		packages.setArrivalTime(LocalDateTime.now());
		packages.setPickupTime(dto.getPickupTime());
		if (dto.getStatus() == null || dto.getStatus().isBlank()) {
			packages.setStatus("未領取");
		} else {
			packages.setStatus(dto.getStatus());
		}
		packages.setType(dto.getType());
		packages.setSign(dto.getSign());
		packages.setPlace(dto.getPlace());

		if (dto.getPhoto() != null) {
			packages.setPhoto(Base64.getDecoder().decode(dto.getPhoto()));
		}

		return packagesRepositroy.save(packages);
	}

	// 查詢包裹
	public List<Packages> findPackagesByUnitId(Integer unitId) {
		Units unit = unitsRepository.findById(unitId)
				.orElseThrow(() -> new IllegalArgumentException("找不到該 unitId：" + unitId));

		return packagesRepositroy.findByUnit(unit);
	}

	// 多條件查詢
	public List<Packages> search(PackagesSearchDTO dto, Integer unitId) {
		return packagesRepositroy.searchByConditions(dto.getStatus(), dto.getType(), dto.getArrivalTime(), unitId);
	}

	// 改變狀態
	@Transactional
	public void markAsPickedUp(Integer packagesId) {
		packagesRepositroy.updateStatusById(packagesId, "已領取");
	}

	// 修改
	public Packages update(Integer id,PackagesDTO dto) {
		Packages packages = packagesRepositroy.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("找不到對應的包裹 ID：" + id));;
				
		if (dto.getCommunityId() != null) {
			Community community = communityRepository.findById(dto.getCommunityId())
					.orElseThrow(() -> new IllegalArgumentException("找不到對應的 communityId：" + dto.getCommunityId()));
			packages.setCommunity(community);
		}
		if (dto.getUnitId() != null) {
			Units unit = unitsRepository.findById(dto.getUnitId())
					.orElseThrow(() -> new IllegalArgumentException("找不到對應的 unitId：" + dto.getUnitId()));
			packages.setUnit(unit);
		}
		packages.setPiece(dto.getPiece());
		packages.setType(dto.getType());
		packages.setPlace(dto.getPlace());

		return packagesRepositroy.save(packages);
	}

}
