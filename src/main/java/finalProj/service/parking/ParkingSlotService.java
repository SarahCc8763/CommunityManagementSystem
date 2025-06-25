package finalProj.service.parking;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import finalProj.domain.parking.ParkingSlot;
import finalProj.dto.parking.ParkingSlotQueryDTO;
import finalProj.repository.UsersRepository;
import finalProj.repository.parking.ParkingSlotRepository;
import finalProj.repository.parking.ParkingTypeRepository;
import jakarta.transaction.Transactional;

//車位資料 Service
@Service
@Transactional
public class ParkingSlotService {

	@Autowired
	private ParkingSlotRepository repository;

	@Autowired
	private ParkingTypeRepository parkingTypeRepository;

	@Autowired
	private UsersRepository usersRepository;


	
	// 查詢所有車位資料
	public List<ParkingSlot> findAll() {
		return repository.findAll();
	}

	// 新增多筆
	public List<ParkingSlot> createAll(List<ParkingSlot> parkingSlots) {
		List<ParkingSlot> savedList = new ArrayList<>();

		for (ParkingSlot parkingSlot : parkingSlots) {
			String slotNumber = parkingSlot.getSlotNumber() != null ? parkingSlot.getSlotNumber().trim() : null;
			Integer parkingTypeId = parkingSlot.getParkingTypeId();
			Integer usersId = parkingSlot.getUsersId();

			// 預設不可承租為 false
			parkingSlot.setIsRentable(parkingSlot.getIsRentable() != null && parkingSlot.getIsRentable());

			// 驗證欄位
			if (slotNumber == null || slotNumber.isEmpty())
				continue;
			if (parkingTypeId == null || !parkingTypeRepository.existsById(parkingTypeId))
				continue;
			if (usersId == null || !usersRepository.existsById(usersId))
				continue;

			savedList.add(repository.save(parkingSlot));
		}

		return savedList;
	}

	// 新增車位資料
	public ParkingSlot create(ParkingSlot parkingSlot) {
		String slotNumber = parkingSlot.getSlotNumber().trim();
		Integer parkingTypeId = parkingSlot.getParkingTypeId();
		Integer usersId = parkingSlot.getUsersId();
		parkingSlot.setIsRentable(parkingSlot.getIsRentable() == null ? false : parkingSlot.getIsRentable());

		if (slotNumber == null || slotNumber == "") {
			return null;
		}

		if (parkingTypeId == null || parkingTypeRepository.findById(parkingTypeId) == null) {
			return null;
		}

		if (usersId == null || usersRepository.findById(usersId) == null) {
			return null;
		}

		return repository.save(parkingSlot);
	}

	// 修改車位資料
	public ParkingSlot update(ParkingSlot parkingSlot) {
		Integer id = parkingSlot.getId();
		String slotNumber = parkingSlot.getSlotNumber().trim();
		Integer parkingTypeId = parkingSlot.getParkingTypeId();
		Integer usersId = parkingSlot.getUsersId();
		parkingSlot.setIsRentable(parkingSlot.getIsRentable() == null ? false : parkingSlot.getIsRentable());

		if (id == null || !repository.existsById(id)) {
			return null;
		}

		if (slotNumber == null || slotNumber == "") {
			return null;
		}

		if (parkingTypeId == null || parkingTypeRepository.findById(parkingTypeId) == null) {
			return null;
		}

		if (usersId == null || usersRepository.findById(usersId) == null) {
			return null;
		}
		return repository.save(parkingSlot);
	}

	// 查詢可抽籤車位
	public List<ParkingSlot> findAvailableSlots(ParkingSlotQueryDTO dto) {
		Integer limit = dto.getLimit();
		Integer typeId = dto.getTypeId();
		Date eventStart = dto.getEventStart();
		Date eventEnd = dto.getEventEnd();

		if (limit == null || typeId == null || eventStart == null || eventEnd == null) {
			return null;
		}
		if (parkingTypeRepository.findById(typeId) == null) {
			return null;
		}

		Pageable pageable = PageRequest.of(0, limit);

		return repository.findAvailableSlotsForEvent(typeId, eventStart, eventEnd, pageable);
	}

	// 刪除資料
	public Boolean delete(Integer id) {
		if (id != null && repository.existsById(id)) {
			repository.deleteById(id);
			return true;
		}
		return false;
	}
}