package finalProj.service.community;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.community.Community;
import finalProj.dto.community.CommunityDTO;
import finalProj.enumCommunity.CommunityFunction;
import finalProj.repository.community.CommunityRepository;
import finalProj.util.CommunityFunctionUtils;

@Service
@Transactional
public class CommunityService {
	@Autowired
	private CommunityRepository communityRepository;

	// 新增Community
	public Community save(Community community) {
		return communityRepository.save(community);
	}

	// 新增CommunityAndFunction
	public Community createCommunityWithFunctions(CommunityDTO dto) {
		Community community = new Community();
		community.setName(dto.getName());
		community.setAddress(dto.getAddress());

		Long functionValue = 0L;
		if (dto.getFunctions() != null) {
			for (String name : dto.getFunctions()) {
				try {
					CommunityFunction cf = CommunityFunction.valueOf(name.toUpperCase());
					functionValue = CommunityFunctionUtils.addFunction(functionValue, cf);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("功能名稱不合法：" + name);
				}
			}
		}
		community.setFunction(functionValue);
		return communityRepository.save(community);
	}

	// 查詢一筆資料
	public Community findById(Integer id) {
		if (id != null) {
			Optional<Community> optional = communityRepository.findById(id);
			if (optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}

	// CommunityID 是否存在
	public boolean exists(Integer id) {
		if (id != null) {
			return communityRepository.existsById(id);
		}
		return false;
	}

	// 刪除一筆資料
	public boolean remove(Integer id) {
		if (id != null) {
			Optional<Community> opt = communityRepository.findById(id);
			if (opt.isPresent()) {
				communityRepository.delete(opt.get());
				return true;
			}
		}
		return false;
	}

	// 修改社區
	public Community update(Integer id, CommunityDTO dto) {
		Optional<Community> optional = communityRepository.findById(id);
		if (optional.isPresent()) {
			Community community = optional.get();
			if (dto.getName() != null) {
				community.setName(dto.getName());
			}
			if (dto.getAddress() != null) {
				community.setAddress(dto.getAddress());
			}
			//修改功能
			if (dto.getFunctions() != null) {
				Long functionValue = 0L;
				for (String name : dto.getFunctions()) {
					try {
						CommunityFunction cf = CommunityFunction.valueOf(name.toUpperCase());
						functionValue = CommunityFunctionUtils.addFunction(functionValue, cf);
					} catch (IllegalArgumentException e) {
						throw new RuntimeException("功能名稱不合法：" + name);
					}
				}
				community.setFunction(functionValue);
			}

			return communityRepository.save(community);

		}
		return null;
	}

	// 找尋所有資料
	public List<Community> findAll() {
		return communityRepository.findAll();
	}

	// 查詢社區名稱
	public List<Community> findByCommunityName(String name) {
		if (name != null) {
			List<Community> result = communityRepository.findByName(name);
			return result.isEmpty() ? null : result;
		}
		return null;
	}
	
	//顯示社區功能
	public List<String> getEnabledFunctionNames(Integer communityId){
		Community community = communityRepository.findById(communityId)
				.orElseThrow(() -> new RuntimeException("找不到社區 ID: " + communityId));
		
		Long functionValue = community.getFunction();
		List<CommunityFunction> enabledFunctions = CommunityFunctionUtils.showAllEnableFunction(functionValue);
		
		   return enabledFunctions.stream()
		            .map(Enum::name)
		            .toList();
	}

}
