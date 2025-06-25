package finalProj.service.community;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.community.Community;
import finalProj.repository.community.CommunityRepository;

@Service
@Transactional
public class CommunityService {
	@Autowired
	private CommunityRepository communityRepository;

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

	// 新增ticket
	public Community save(Community community) {
		return communityRepository.save(community);
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
	public Community update(Integer id, Community community) {
		if (communityRepository.existsById(id)) {
			community.setCommunityId(id); // 確保 ID 正確綁定
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

}
