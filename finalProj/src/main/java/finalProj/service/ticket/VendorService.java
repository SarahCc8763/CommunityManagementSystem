package finalProj.service.ticket;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domin.ticket.Vendor;
import finalProj.repository.ticket.VendorRepository;

@Service
@Transactional
public class VendorService {

	@Autowired
	private VendorRepository vendorRepository;

	// 查詢一筆資料
	public Vendor findById(Integer id) {
		if (id != null) {
			Optional<Vendor> optional = vendorRepository.findById(id);
			if (optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}

	// 新增ticket
	public Vendor save(Vendor vendor) {
		return vendorRepository.save(vendor);
	}

	// ticket id 是否存在
	public boolean exists(Integer id) {
		if (id != null) {
			return vendorRepository.existsById(id);
		}
		return false;
	}

	// 刪除一筆資料
	public boolean remove(Integer id) {
		if (id != null) {
			if (vendorRepository.existsById(id)) {
				vendorRepository.deleteById(id);
				return true;
			}
		}
		return false;
	}

	// 修改ticket
	public Vendor update(Integer id, Vendor vendor) {
		if (vendorRepository.existsById(id)) {
			vendor.setVendorID(id); // 確保 ID 正確綁定
			return vendorRepository.save(vendor);
		}
		return null;
	}

	// 找尋所有資料
	public List<Vendor> findAll() {
		return vendorRepository.findAll();
	}

}
