package finalProj.service.parking;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.parking.ParkingRentals;
import finalProj.domain.parking.ParkingSlot;
import finalProj.repository.parking.ParkingRentalsRepository;
import finalProj.repository.parking.ParkingSlotRepository;
import finalProj.repository.users.UsersRepository;
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

	// 查詢可承租車位
	public List<ParkingSlot> findAvailableSlots(Integer parkingTypeId, Date start, Date end) {
		return repository.findAvailableSlotsByTypeAndPeriod(parkingTypeId, start, end);
	}

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
		Integer usersId = rental.getUsers().getUsersId();
		Integer parkingSlotId = rental.getParkingSlot().getId();
		Date rentBuyStart = rental.getRentBuyStart();
		Date rentEnd = rental.getRentEnd();
		String licensePlate = rental.getLicensePlate();
		rental.setStatus(rental.getStatus() == null ? false : rental.getStatus());

		if (usersId == null || usersRepository.findById(usersId) == null) {
			throw new IllegalArgumentException("使用者不可為空");
		}

		if (parkingSlotId == null || parkingSlotRepository.findById(parkingSlotId) == null) {
			throw new IllegalArgumentException("車位不可為空");
		}

		if (rentBuyStart == null) {
			throw new IllegalArgumentException("承租起始日不可為空");
		}

		if (rentEnd == null) {
			throw new IllegalArgumentException("承租截止日不可為空");
		}

		if (licensePlate == null || licensePlate == "") {
			throw new IllegalArgumentException("車牌不可為空");
		}

		// 檢查時間是否重疊
		if (isOverlapping(rentBuyStart, rentEnd, parkingSlotId, null)) {
			throw new IllegalArgumentException("該車位於該時段已被租用");
		}
		return repository.save(rental);
	}

	// 修改承租紀錄
	public ParkingRentals update(ParkingRentals rental) {
		Integer id = rental.getId();
		Integer usersId = rental.getUsers().getUsersId();
		Integer parkingSlotId = rental.getParkingSlot().getId();
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