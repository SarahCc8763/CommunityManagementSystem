package finalProj.service.parking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.parking.ParkingType;
import finalProj.repository.parking.ParkingTypeRepository;
import jakarta.transaction.Transactional;

// 車位種類 Service
@Service
@Transactional
public class ParkingTypeService {

	@Autowired
	private ParkingTypeRepository repository;

	// 查詢所有車位種類
	public List<ParkingType> findAll() {
		return repository.findAll();
	}

	// 新增車位種類
	public ParkingType create(ParkingType parkingType) {
		String type = parkingType.getType().trim();

		if (type != null && type != "" && !repository.existsByType(type)) {
			return repository.save(parkingType);
		}
		return null;
	}

	// 修改車位種類
	public ParkingType update(ParkingType parkingType) {
		Integer id = parkingType.getId();

		if (id != null && repository.existsById(id)) {
			return repository.save(parkingType);
		}
		return null;
	}

	// 刪除種類
	public Boolean delete(Integer id) {
		if (id != null && repository.existsById(id)) {
			repository.deleteById(id);
			return true;
		}
		return false;
	}
}
