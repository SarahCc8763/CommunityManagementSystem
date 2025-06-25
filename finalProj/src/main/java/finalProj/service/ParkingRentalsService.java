package finalProj.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.ParkingRentals;
import finalProj.repository.ParkingRentalsRepository;
import finalProj.repository.ParkingSlotRepository;
import finalProj.repository.UsersRepository;
import jakarta.transaction.Transactional;

// 承租紀錄 Service
@Service
@Transactional
public class ParkingRentalsService {

	@Autowired
	private ParkingRentalsRepository repository;

	@Autowired
	private ParkingSlotRepository parkingSlotRepository;

	@Autowired
	private UsersRepository usersRepository;

	// 查詢所有承租資料
	public List<ParkingRentals> findAll() {
		return repository.findAll();
	}

	// 驗證時段重疊
	public boolean isOverlapping(Date newStart, Date newEnd, Integer parkingSlotId, Integer excludeId) {
		List<ParkingRentals> existing = repository.findByParkingSlotId(parkingSlotId);
		if (existing == null) {
			return false;
		}
		for (ParkingRentals app : existing) {
			if (excludeId != null && app.getId().equals(excludeId)) {
	            continue; // 排除自己這筆資料
	        }
			Date existingStart = app.getRentBuyStart();
			Date existingEnd = app.getRentEnd();
			if (newStart.before(existingEnd) && newEnd.after(existingStart)) {
				return true; // 有重疊
			}
		}
		return false;
	}

	// 新增承租紀錄
	public ParkingRentals create(ParkingRentals rental) {
		Integer usersId = rental.getUsersId();
		Integer parkingSlotId = rental.getParkingSlotId();
		Date rentBuyStart = rental.getRentBuyStart();
		Date rentEnd = rental.getRentEnd();
		String licensePlate = rental.getLicensePlate();
		rental.setStatus(rental.getStatus() == null ? false : rental.getStatus());

		if (usersId == null || usersRepository.findById(usersId) == null) {
			return null;
		}

		if (parkingSlotId == null || parkingSlotRepository.findById(parkingSlotId) == null) {
			return null;
		}

		if (rentBuyStart == null || rentEnd == null) {
			return null;
		}

		if (licensePlate == null || licensePlate == "") {
			return null;
		}

		// 檢查時間是否重疊
		if (isOverlapping(rentBuyStart, rentEnd, parkingSlotId, null)) {
			return null; // 或者丟出例外、回傳錯誤訊息
		}

		return repository.save(rental);
	}

	// 修改承租紀錄
	public ParkingRentals update(ParkingRentals rental) {
		Integer id = rental.getId();
		Integer usersId = rental.getUsersId();
		Integer parkingSlotId = rental.getParkingSlotId();
		Date rentBuyStart = rental.getRentBuyStart();
		Date rentEnd = rental.getRentEnd();
		String licensePlate = rental.getLicensePlate();
		rental.setStatus(rental.getStatus() == null ? false : rental.getStatus());

		if (id == null || !repository.existsById(id)) {
			return null;
		}

		if (usersId == null || usersRepository.findById(usersId) == null) {
			return null;
		}

		if (parkingSlotId == null || parkingSlotRepository.findById(parkingSlotId) == null) {
			return null;
		}

		if (rentBuyStart == null || rentEnd == null) {
			return null;
		}

		if (licensePlate == null || licensePlate == "") {
			return null;
		}
		
		// 檢查時間是否重疊
		if (isOverlapping(rentBuyStart, rentEnd, parkingSlotId, id)) {
			return null; // 或者丟出例外、回傳錯誤訊息
		}

		return repository.save(rental);
	}

	// 刪除承租紀錄
	public Boolean delete(Integer id) {
		if (id != null && repository.existsById(id)) {
			repository.deleteById(id);
			return true;
		}
		return false;
	}
}