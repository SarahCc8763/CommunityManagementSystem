package finalProj.service.parking;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.community.Community;
import finalProj.domain.parking.ParkingType;
import finalProj.repository.community.CommunityRepository;
import finalProj.repository.parking.ParkingTypeRepository;
import jakarta.transaction.Transactional;

// 車位種類 Service
@Service
@Transactional
public class ParkingTypeService {

	@Autowired
	private ParkingTypeRepository repository;

	@Autowired
	private CommunityRepository communityRepository;

	// 修改社區車位
	public List<ParkingType> replaceTypesByCommunity(Integer communityId, List<ParkingType> inputTypes) {
		Optional<Community> communityOpt = communityRepository.findById(communityId);
		if (communityOpt.isEmpty()) {
			return null;
		}

		Community community = communityOpt.get();

		// 1. 找出現有的種類
		List<ParkingType> existing = repository.findByCommunity_CommunityId(communityId);
		Set<String> existingTypeNames = existing.stream().map(ParkingType::getType).collect(Collectors.toSet());

		Set<String> newTypeNames = inputTypes.stream().map(ParkingType::getType).collect(Collectors.toSet());

		// 2. 刪除原有但不在新資料裡的項目
		List<ParkingType> toDelete = existing.stream().filter(pt -> !newTypeNames.contains(pt.getType())).toList();
		repository.deleteAll(toDelete);

		// 3. 新增新的項目（如果還沒存在）
		List<ParkingType> toAdd = inputTypes.stream().filter(pt -> !existingTypeNames.contains(pt.getType()))
				.map(pt -> {
					pt.setCommunity(community);
					return pt;
				}).toList();

		repository.saveAll(toAdd);

		// 4. 回傳目前全部的
		return repository.findByCommunity_CommunityId(communityId);
	}

}
