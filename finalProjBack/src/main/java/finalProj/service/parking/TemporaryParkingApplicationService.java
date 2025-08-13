package finalProj.service.parking;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.parking.TemporaryParkingApplication;
import finalProj.repository.parking.ParkingSlotRepository;
import finalProj.repository.parking.ParkingTypeRepository;
import finalProj.repository.parking.TemporaryParkingApplicationRepository;
import jakarta.transaction.Transactional;

// 臨停申請紀錄 Service
@Service
@Transactional
public class TemporaryParkingApplicationService {

	@Autowired
	private TemporaryParkingApplicationRepository repository;

	@Autowired
	private ParkingTypeRepository parkingTypeRepository;

	@Autowired
	private ParkingSlotRepository parkingSlotRepository;

	// 查詢所有臨停申請
	public List<TemporaryParkingApplication> findAll() {
		return repository.findAll();
	}

	// 驗證時段重疊
	public boolean isOverlapping(Date newStart, Date newEnd, Integer parkingSlotId, Integer excludeId) {
		List<TemporaryParkingApplication> existing = repository.findByParkingSlotId(parkingSlotId);
		if (existing == null) {
			return false;
		}
		for (TemporaryParkingApplication app : existing) {
			if (excludeId != null && app.getId().equals(excludeId)) {
	            continue; // 排除自己這筆資料
	        }
			
			Date existingStart = app.getStartTime();
			Date existingEnd = app.getEndTime();
			if (newStart.before(existingEnd) && newEnd.after(existingStart)) {
				return true; // 有重疊
			}
		}
		return false;
	}

	// 新增臨停申請
	public TemporaryParkingApplication create(TemporaryParkingApplication application) {
		String visitorName = application.getVisitorName();
		String licensePlate = application.getLicensePlate();
		Integer parkingTypeId = application.getParkingType().getId();
		Date startTime = application.getStartTime();
		Date endTime = application.getEndTime();
		String purpose = application.getPurpose();
		Integer parkingSlotId = application.getParkingSlot().getId();
		application.setRequestTime(new Date());

		if (visitorName == null || visitorName == "") {
			return null;
		}

		if (licensePlate == null || licensePlate == "") {
			return null;
		}

		if (purpose == null || purpose == "") {
			return null;
		}

		if (parkingTypeId == null || parkingTypeRepository.findById(parkingTypeId) == null) {
			return null;
		}

		if (parkingTypeId == null || parkingSlotRepository.findById(parkingSlotId) == null) {
			return null;
		}

		if (startTime == null || endTime == null) {
			return null;
		}

		// 檢查時間是否重疊
		if (isOverlapping(startTime, endTime, parkingSlotId, null)) {
			return null; // 或者丟出例外、回傳錯誤訊息
		}
		return repository.save(application);
	}

	// 修改臨停申請
	public TemporaryParkingApplication update(TemporaryParkingApplication application) {
		Integer id = application.getId();
		String visitorName = application.getVisitorName();
		String licensePlate = application.getLicensePlate();
		Integer parkingTypeId = application.getParkingType().getId();
		Date startTime = application.getStartTime();
		Date endTime = application.getEndTime();
		String purpose = application.getPurpose();
		Integer parkingSlotId = application.getParkingSlot().getId();
		
		if (id == null || !repository.existsById(id)) {
			return null;
		}

		if (visitorName == null || visitorName == "") {
			return null;
		}

		if (licensePlate == null || licensePlate == "") {
			return null;
		}

		if (purpose == null || purpose == "") {
			return null;
		}

		if (parkingTypeId == null || parkingTypeRepository.findById(parkingTypeId) == null) {
			return null;
		}

		if (parkingTypeId == null || parkingSlotRepository.findById(parkingSlotId) == null) {
			return null;
		}

		if (startTime == null || endTime == null) {
			return null;
		}

		// 檢查時間是否重疊
		if (isOverlapping(startTime, endTime, parkingSlotId, id)) {
			return null; // 或者丟出例外、回傳錯誤訊息
		}

		return repository.save(application);
	}

	// 刪除臨停申請
	public Boolean delete(Integer id) {
		if (id != null && repository.existsById(id)) {
			repository.deleteById(id);
			return true;
		}
		return false;
	}

}