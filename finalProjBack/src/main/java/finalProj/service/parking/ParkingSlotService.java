package finalProj.service.parking;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import finalProj.domain.community.Community;
import finalProj.domain.parking.ParkingSlot;
import finalProj.domain.parking.ParkingType;
import finalProj.domain.users.Users;
import finalProj.dto.parking.ParkingSlotDTO;
import finalProj.dto.parking.ParkingSlotQueryDTO;
import finalProj.repository.CommunityRepository;
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
	private CommunityRepository communityRepository;

	@Autowired
	private ParkingTypeRepository parkingTypeRepository;

	@Autowired
	private UsersRepository usersRepository;

//	// 查詢所有車位資料
//	public List<ParkingSlot> findAll() {
//		return repository.findAll();
//	}

	public List<ParkingSlot> createAllFromDto(List<ParkingSlotDTO> dtos, Integer communityId) {
		Community community = communityRepository.findById(communityId).orElse(null);
		if (community == null) {
			throw new IllegalArgumentException("找不到社區");
		}

		List<ParkingSlot> converted = new ArrayList<>();

		for (ParkingSlotDTO dto : dtos) {
			ParkingSlot slot = new ParkingSlot();
			slot.setSlotNumber(dto.getSlotNumber());
			slot.setLocation(dto.getLocation());
			slot.setLicensePlate(dto.getLicensePlate());
			slot.setIsRentable(dto.getIsRentable());
			slot.setCommunity(community); // 每筆都設同一個社區

			// 先建立對應的 ParkingType 和 Users 物件放進去，交由 createAll 處理驗證與實體查找
			if (dto.getParkingType() != null) {
				ParkingType pt = parkingTypeRepository
						.findByTypeAndCommunity_CommunityId(dto.getParkingType(), communityId).orElse(null);
				slot.setParkingType(pt);
			}

			if (dto.getUsersId() != null) {
				Users u = new Users();
				u.setUsersId(dto.getUsersId());
				slot.setUsers(u);
			}

			converted.add(slot);
		}

		return createAll(converted); // 呼叫內部的驗證與 save 方法
	}

	// 新增多筆
	public List<ParkingSlot> createAll(List<ParkingSlot> parkingSlots) {
		List<ParkingSlot> savedList = new ArrayList<>();

		for (ParkingSlot parkingSlot : parkingSlots) {
			// Trim slot number
			String slotNumber = parkingSlot.getSlotNumber() != null ? parkingSlot.getSlotNumber().trim() : null;
			parkingSlot.setSlotNumber(slotNumber); // 回存去空白的 slotNumber

			// 預設 isRentable 為 false（避免 null）
			parkingSlot.setIsRentable(Boolean.TRUE.equals(parkingSlot.getIsRentable()));

			ParkingType parkingType = parkingSlot.getParkingType();
			Users users = parkingSlot.getUsers();

			Integer parkingTypeId = (parkingType != null) ? parkingType.getId() : null;
			Integer usersId = (users != null) ? users.getUsersId() : null;

			// 驗證欄位
			Set<String> existingSlots = repository.findByCommunity(parkingSlot.getCommunity()).stream()
					.map(ParkingSlot::getSlotNumber).collect(Collectors.toSet());

			if (existingSlots.contains(slotNumber)) {
				continue;
			}
			if (repository.existsByCommunityAndSlotNumber(parkingSlot.getCommunity(), slotNumber)) {
				continue; // 或拋出例外提示重複
			}
			if (slotNumber == null || slotNumber.isEmpty())
				continue;
			if (parkingTypeId == null || !parkingTypeRepository.existsById(parkingTypeId))
				continue;
			if (usersId == null || !usersRepository.existsById(usersId))
				continue;

			parkingType = parkingTypeRepository.findById(parkingTypeId).orElse(null);
			if (parkingType == null || parkingType.getCommunity() == null)
				continue;

			users = usersRepository.findById(usersId).orElse(null);
			if (users == null || users.getCommunity() == null)
				continue;

			parkingSlot.setParkingType(parkingType);
			parkingSlot.setUsers(users);

			savedList.add(repository.save(parkingSlot));
		}

		return savedList;
	}

	// 新增車位資料
	public ParkingSlot create(ParkingSlotDTO dto, Integer communityId) {
		Community community = communityRepository.findById(communityId).orElse(null);
		if (community == null)
			return null;

		ParkingSlot slot = new ParkingSlot();
		slot.setSlotNumber(dto.getSlotNumber());
		slot.setLocation(dto.getLocation());
		slot.setLicensePlate(dto.getLicensePlate());
		slot.setIsRentable(Boolean.TRUE.equals(dto.getIsRentable()));
		slot.setCommunity(community);

		if (dto.getParkingTypeId() != null) {
			ParkingType pt = parkingTypeRepository.findById(dto.getParkingTypeId()).orElse(null);
			slot.setParkingType(pt);
		} else if (dto.getParkingType() != null) {
			// 備用查詢：如果有提供 type 名稱也支援
			ParkingType pt = parkingTypeRepository.findByTypeAndCommunity_CommunityId(dto.getParkingType(), communityId)
					.orElse(null);
			slot.setParkingType(pt);
		}

		if (dto.getUsersId() != null) {
			Users u = usersRepository.findById(dto.getUsersId()).orElse(null);
			slot.setUsers(u);
		}

		// 套用 create(List) 裡的驗證邏輯
		List<ParkingSlot> savedList = createAll(List.of(slot));
		return savedList.isEmpty() ? null : savedList.get(0);
	}

	// 修改車位資料
	public ParkingSlot update(ParkingSlotDTO dto) {
		if (dto.getId() == null) {
			return null;
		}

		Optional<ParkingSlot> optionalSlot = repository.findById(dto.getId());
		if (optionalSlot.isEmpty()) {
			return null;
		}

		ParkingSlot parkingSlot = optionalSlot.get();

		// 更新欄位
		String slotNumber = dto.getSlotNumber() != null ? dto.getSlotNumber().trim() : null;
		if (slotNumber == null || slotNumber.isEmpty()) {
			return null;
		}
		parkingSlot.setSlotNumber(slotNumber);
		parkingSlot.setLocation(dto.getLocation());
		parkingSlot.setLicensePlate(dto.getLicensePlate());
		parkingSlot.setIsRentable(Boolean.TRUE.equals(dto.getIsRentable()));

		// 更新關聯：Community 不允許變更，因此保留原本的

		// 更新 ParkingType
		if (dto.getParkingTypeId() != null) {
			ParkingType pt = parkingTypeRepository.findById(dto.getParkingTypeId()).orElse(null);
			if (pt == null)
				return null;
			parkingSlot.setParkingType(pt);
		} else if (dto.getParkingType() != null) {
			ParkingType pt = parkingTypeRepository.findByTypeAndCommunity_CommunityId(dto.getParkingType(),
					parkingSlot.getCommunity().getCommunityId()).orElse(null);
			if (pt == null)
				return null;
			parkingSlot.setParkingType(pt);
		}

		// 更新使用者
		if (dto.getUsersId() != null) {
			Users u = usersRepository.findById(dto.getUsersId()).orElse(null);
			if (u == null)
				return null;
			parkingSlot.setUsers(u);
		}

		return repository.save(parkingSlot);
	}

	// 查詢可抽籤車位
	public List<ParkingSlot> findAvailableSlots(ParkingSlotQueryDTO dto) {
		Integer limit = dto.getLimit();
		Integer communityId = dto.getCommunityId();
		Integer typeId = dto.getTypeId();
		Date eventStart = dto.getEventStart();
		Date eventEnd = dto.getEventEnd();

		// 基本欄位驗證
		if (limit == null || communityId == null || typeId == null || eventStart == null || eventEnd == null) {
			return null;
		}

		// 驗證資料是否存在
		if (!communityRepository.existsById(communityId)) {
			return null;
		}
		if (!parkingTypeRepository.existsById(typeId)) {
			return null;
		}

		Pageable pageable = PageRequest.of(0, limit);
		return repository.findAvailableSlotsForEvent(communityId, typeId, eventStart, eventEnd, pageable);
	}

	// 刪除資料
	public boolean delete(Integer id) {
		if (id == null) {
			return false;
		}

		if (!repository.existsById(id)) {
			return false;
		}

		repository.deleteById(id);
		return true;
	}

}