package finalProj.service.ticket;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.ticket.IssueType;
import finalProj.repository.ticket.IssueTypeRepository;

@Service
@Transactional
public class IssueTypeService {
	
	@Autowired
	private IssueTypeRepository issueTypeRepository;
	
	// 查詢一筆資料
		public IssueType findById(Integer id) {
			if (id != null) {
				Optional<IssueType> optional = issueTypeRepository.findById(id);
				if (optional.isPresent()) {
					return optional.get();
				}
			}
			return null;
		}

		// 新增ticket
		public IssueType save(IssueType issueType) {
			return issueTypeRepository.save(issueType);
		}

		// ticket id 是否存在
		public boolean exists(Integer id) {
			if (id != null) {
				return issueTypeRepository.existsById(id);
			}
			return false;
		}

		// 刪除一筆資料
		public boolean remove(Integer id) {
			if (id != null) {
				if (issueTypeRepository.existsById(id)) {
					issueTypeRepository.deleteById(id);
					return true;
				}
			}
			return false;
		}

		// 修改ticket
		public IssueType update(Integer id, IssueType issueType) {
			if (issueTypeRepository.existsById(id)) {
				issueType.setId(id); // 確保 ID 正確綁定
				return issueTypeRepository.save(issueType);
			}
			return null;
		}

		// 找尋所有資料
		public List<IssueType> findAll() {
			return issueTypeRepository.findAll();
		}


}
