package finalProj.service.parking;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.community.Community;
import finalProj.domain.notifications.Notifications;
import finalProj.domain.notifications.UnitsNotifications;
import finalProj.domain.parking.ParkingRentals;
import finalProj.domain.parking.ParkingSlot;
import finalProj.domain.users.Units;
import finalProj.domain.users.Users;
import finalProj.repository.notifications.NotificationsRepository;
import finalProj.repository.notifications.UnitsNotificationsRepository;
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

	@Autowired
	private NotificationsRepository notificationsRepository;

	@Autowired
	private UnitsNotificationsRepository unitsNotificationsRepository;

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
		String slotNumber = rental.getParkingSlot().getSlotNumber();
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

		ParkingRentals result = repository.save(rental);

		// === 建立承租車位 Notifications ===
		if (result != null && !rental.getUsers().getRolesUsersList().isEmpty()
				&& rental.getUsers().getRolesUsersList().get(0).getRole().getRolesId() != 2) {
			Notifications notification = new Notifications();
			notification.setTitle("承租成功通知");
			notification.setDescription("您已成功承租車位 " + slotNumber + "<br>承租期間為 " + rentBuyStart.toInstant()
					.atZone(ZoneId.systemDefault())
					.toLocalDate()
					.format(DateTimeFormatter.ofPattern("yyyy年MM月")) + " 至 "
					+ rentEnd.toInstant()
							.atZone(ZoneId.systemDefault())
							.toLocalDate()
							.format(DateTimeFormatter.ofPattern("yyyy年MM月"))
					+ "。");
			notification.setCreatedTime(LocalDateTime.now());
			notification.setCommunity(rental.getCommunity());
			notificationsRepository.save(notification);

			UnitsNotifications unitsNotifications = new UnitsNotifications();
			unitsNotifications.setNotifications(notification);
			Units unit = rental.getUsers().getUnitsUsersList().get(0).getUnit();
			unitsNotifications.setUnits(unit);
			unitsNotificationsRepository.save(unitsNotifications);
		}

		return result;
	}

	// 修改承租紀錄
	public ParkingRentals update(ParkingRentals rental) {
		Integer id = rental.getId();
		Integer usersId = rental.getUsers().getUsersId();
		Boolean approved = rental.getApproved();
		Integer parkingSlotId = rental.getParkingSlot().getId();
		Date rentBuyStart = rental.getRentBuyStart();
		Date rentEnd = rental.getRentEnd();
		String licensePlate = rental.getLicensePlate();
		rental.setStatus(rental.getStatus() == null ? false : rental.getStatus());

		if (id == null || !repository.existsById(id)) {
			throw new IllegalArgumentException("無該筆承租紀錄");
		}

		if (usersId == null || usersRepository.findById(usersId) == null) {
			throw new IllegalArgumentException("使用者不可為空");
		}

		if (parkingSlotId == null || parkingSlotRepository.findById(parkingSlotId).isEmpty()) {
			throw new IllegalArgumentException("車位不可為空");
		}

		if (rentBuyStart == null || rentEnd == null) {
			throw new IllegalArgumentException("承租起始截止日不可為空");
		}

		if (licensePlate == null || licensePlate == "") {
			throw new IllegalArgumentException("車牌不可為空");
		}

		// 檢查時間是否重疊
		if (isOverlapping(rentBuyStart, rentEnd, parkingSlotId, id)) {
			throw new IllegalArgumentException("該車位於該時段已被租用");
		}

		ParkingRentals result = repository.save(rental);

		// === 建立承租車位 Notifications ===
		if (approved && result != null && !rental.getUsers().getRolesUsersList().isEmpty()
				&& rental.getUsers().getRolesUsersList().get(0).getRole().getRolesId() != 2) {
			Notifications notification = new Notifications();
			notification.setTitle("承租車位審核通知");
			String slotNumber = rental.getParkingSlot().getSlotNumber();
			notification.setDescription("管理員已審核您的承租車位 " + slotNumber + "<br>承租期間為 " + rentBuyStart.toInstant()
					.atZone(ZoneId.systemDefault())
					.toLocalDate()
					.format(DateTimeFormatter.ofPattern("yyyy年MM月")) + " 至 "
					+ rentEnd.toInstant()
							.atZone(ZoneId.systemDefault())
							.toLocalDate()
							.format(DateTimeFormatter.ofPattern("yyyy年MM月"))
					+ "。");
			notification.setCreatedTime(LocalDateTime.now());
			notification.setCommunity(rental.getCommunity());
			notificationsRepository.save(notification);

			UnitsNotifications unitsNotifications = new UnitsNotifications();
			unitsNotifications.setNotifications(notification);
			Units unit = rental.getUsers().getUnitsUsersList().get(0).getUnit();
			unitsNotifications.setUnits(unit);
			unitsNotificationsRepository.save(unitsNotifications);
		}

		return result;
	}

	// 刪除承租紀錄
	public Boolean delete(Integer id) {
		if (id != null && repository.existsById(id)) {
			ParkingRentals rental = repository.findById(id).orElse(null);
			if (rental == null)
				return false;

			// 儲存必要資訊再刪
			String slotNumber = rental.getParkingSlot().getSlotNumber();
			Date rentBuyStart = rental.getRentBuyStart();
			Date rentEnd = rental.getRentEnd();
			Users user = rental.getUsers();
			Community community = rental.getCommunity();

			// 儲存後刪除
			repository.deleteById(id);

			// === 建立通知 ===
			if (user != null && !user.getRolesUsersList().isEmpty()
					&& user.getRolesUsersList().get(0).getRole().getRolesId() != 2) {

				Notifications notification = new Notifications();
				notification.setTitle("承租紀錄刪除通知");
				notification.setDescription("您原先承租的車位 " + slotNumber + "<br>承租期間為 " +
						rentBuyStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
								.format(DateTimeFormatter.ofPattern("yyyy年MM月"))
						+
						" 至 " +
						rentEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
								.format(DateTimeFormatter.ofPattern("yyyy年MM月"))
						+
						" 已被刪除。");
				notification.setCreatedTime(LocalDateTime.now());
				notification.setCommunity(community);
				notificationsRepository.save(notification);

				// 戶號通知綁定
				if (user.getUnitsUsersList() != null && !user.getUnitsUsersList().isEmpty()) {
					Units unit = user.getUnitsUsersList().get(0).getUnit();
					if (unit != null) {
						UnitsNotifications unitsNotifications = new UnitsNotifications();
						unitsNotifications.setNotifications(notification);
						unitsNotifications.setUnits(unit);
						unitsNotificationsRepository.save(unitsNotifications);
					}
				}
			}

			return true;
		}
		return false;
	}

}