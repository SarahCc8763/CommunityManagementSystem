package finalProj.service.parking;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.bulletin.Bulletin;
import finalProj.domain.bulletin.BulletinCategory;
import finalProj.domain.community.Community;
import finalProj.domain.parking.LotteryApply;
import finalProj.domain.parking.LotteryEventSpace;
import finalProj.domain.parking.LotteryEvents;
import finalProj.domain.parking.ParkingSlot;
import finalProj.domain.parking.ParkingType;
import finalProj.domain.users.Users;
import finalProj.dto.parking.DrawLotsResultDTO;
import finalProj.dto.parking.LotteryEventSpaceDTO;
import finalProj.dto.parking.LotteryEventUpdateRequest;
import finalProj.dto.parking.LotteryParticipantDTO;
import finalProj.dto.parking.WinnerDTO;
import finalProj.repository.bulletin.BulletinRepository;
import finalProj.repository.community.CommunityRepository;
import finalProj.repository.parking.LotteryApplyRepository;
import finalProj.repository.parking.LotteryEventRepository;
import finalProj.repository.parking.LotteryEventSpaceRepository;
import finalProj.repository.parking.ParkingSlotRepository;
import finalProj.repository.parking.ParkingTypeRepository;
import finalProj.repository.users.UsersRepository;
import finalProj.service.bulletin.BulletinService;
import jakarta.transaction.Transactional;

// 抽籤活動 Service
@Service
@Transactional
public class LotteryEventService {

	@Autowired
	private LotteryEventRepository repository;

	@Autowired
	private BulletinRepository bulletinEventRepository;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private LotteryEventSpaceRepository lotteryEventSpaceRepository;

	@Autowired
	private LotteryApplyRepository lotteryApplyRepository;

	@Autowired
	private ParkingSlotRepository parkingSlotRepository;

	@Autowired
	private CommunityRepository communityRepository;

	@Autowired
	private ParkingTypeRepository parkingTypeRepository;

	@Autowired
	private BulletinService bulletinService;

	// 查詢所有抽籤活動
	public List<LotteryEvents> findAll() {
		return repository.findAll();
	}

	// 新增抽籤活動
	public LotteryEventUpdateRequest create(LotteryEventUpdateRequest event, Integer communityId) {
		String title = event.getTitle();
		Integer typeId = event.getTypeId();
		Date startedAt = event.getStartedAt();
		Date endedAt = event.getEndedAt();
		Date rentalStart = event.getRentalStart();
		Date rentalEnd = event.getRentalEnd();
		Integer usersId = event.getUsersId();
		Set<LotteryEventSpaceDTO> parkingSlots = event.getParkingSlotIds();

		if (parkingSlots == null || parkingSlots.isEmpty()) {
			return null;
		}

		if (title == null || title.trim().isEmpty()) {
			return null;
		}

		if (startedAt == null || endedAt == null || rentalStart == null || rentalEnd == null) {
			return null;
		}

		Users user = usersRepository.findByUsersId(usersId);
		if (user == null) {
			return null;
		}

		Community community = communityRepository.findByCommunityId(communityId);
		// 建立 Bulletin
		Bulletin bulletin = new Bulletin();
		bulletin.setTitle(title);
		bulletin.setDescription("請參考車位抽籤活動頁面：" + title);
		BulletinCategory bulletinCategory = new BulletinCategory();
		bulletinCategory.setName("抽籤");
		bulletinCategory.setCommunity(community);
		bulletin.setCategory(bulletinCategory);
		bulletin.setUser(user);
		bulletin.setPostTime(LocalDateTime.now());
		bulletin.setRemoveTime(LocalDateTime.ofInstant(
				endedAt.toInstant(),
				ZoneId.of("Asia/Taipei")));
		bulletin.setCommunity(community);
		bulletin = bulletinService.insert(bulletin);

		// 建立 LotteryEvents 並設定 bulletin 的 ID 為主鍵
		LotteryEvents eventEntity = new LotteryEvents();
		eventEntity.setBulletin(bulletin); // Bulletin 的 ID = LotteryEvents 的 ID
		eventEntity.setTitle(title);
		eventEntity
				.setParkingType(parkingTypeRepository.findById(typeId).get());
		eventEntity.setStartedAt(startedAt);
		eventEntity.setEndedAt(endedAt);
		eventEntity.setRentalStart(rentalStart);
		eventEntity.setRentalEnd(rentalEnd);
		eventEntity.setUsers(user);
		eventEntity.setCreatedAt(new Date());
		eventEntity.setStatus(false); // 初始設定為未抽籤
		repository.save(eventEntity);

		List<LotteryEventSpaceDTO> distinctParkingSlots = parkingSlots.stream()
				.collect(Collectors.collectingAndThen(
						Collectors.toMap(LotteryEventSpaceDTO::getParkingSlotId, dto -> dto, (a, b) -> a),
						m -> new ArrayList<>(m.values())));

		// 新增車位清單
		List<LotteryEventSpace> newSpaces = distinctParkingSlots.stream().map(slot -> {
			LotteryEventSpace s = new LotteryEventSpace();
			s.setLotteryEvents(eventEntity);
			s.setParkingSlot(parkingSlotRepository.findById(slot.getParkingSlotId()).get());
			return s;
		}).toList();

		List<LotteryEventSpace> savedSpaces = lotteryEventSpaceRepository.saveAll(newSpaces);

		// 組裝回傳 DTO
		LotteryEventUpdateRequest response = new LotteryEventUpdateRequest();
		response.setId(eventEntity.getBulletinId());
		response.setTitle(eventEntity.getTitle());
		response.setStartedAt(eventEntity.getStartedAt());
		response.setEndedAt(eventEntity.getEndedAt());
		response.setRentalStart(eventEntity.getRentalStart());
		response.setRentalEnd(eventEntity.getRentalEnd());
		response.setUsersId(eventEntity.getUsers().getUsersId());
		response.setCreatedAt(eventEntity.getCreatedAt());
		response.setTypeName(eventEntity.getParkingType().getType());

		Set<LotteryEventSpaceDTO> spaceDTOs = savedSpaces.stream().map(space -> {
			LotteryEventSpaceDTO dto = new LotteryEventSpaceDTO();
			dto.setId(space.getId());
			dto.setParkingSlotId(space.getParkingSlot().getId());
			return dto;
		}).collect(Collectors.toSet());

		response.setParkingSlotIds(spaceDTOs);

		return response;
	}

	// 修改抽籤活動
	public LotteryEventUpdateRequest update(LotteryEventUpdateRequest event) {
		Optional<LotteryEvents> existingOp = repository.findById(event.getId());
		if (existingOp.isEmpty()) {
			return null;
		}

		LotteryEvents existing = existingOp.get();
		System.out.println(event.getStatus());

		String title = event.getTitle();
		Date createdAt = event.getCreatedAt();
		Date startedAt = event.getStartedAt();
		Date endedAt = event.getEndedAt();
		Date rentalStart = event.getRentalStart();
		Date rentalEnd = event.getRentalEnd();
		Integer id = event.getId();
		Integer usersId = event.getUsersId();
		Boolean status = event.getStatus();
		Set<LotteryEventSpaceDTO> parkingSlots = event.getParkingSlotIds();

		if (parkingSlots == null || parkingSlots.isEmpty()) {
			return null; // 或 throw new RuntimeException("未選擇車位")
		}

		if (title == null || title.trim().isEmpty()) {
			return null;
		}

		if (startedAt == null || endedAt == null || createdAt == null || rentalStart == null || rentalEnd == null) {
			return null;
		}

		Users user = usersRepository.findByUsersId(usersId);
		if (user == null) {
			return null;
		}

		ParkingType type = parkingTypeRepository.findById(event.getTypeId()).get();
		if (type == null) {
			return null;
		}

		Optional<Bulletin> bulletinOp = bulletinEventRepository.findById(id);
		if (bulletinOp.isEmpty()) {
			return null;
		}
		// 更新 Bulletin
		Bulletin bulletin = bulletinOp.get();
		bulletin.setTitle(title);
		bulletin.setDescription("請參考車位抽籤活動頁面：" + title);
		bulletin.setUser(user);

		bulletin.setRemoveTime(LocalDateTime.ofInstant(
				endedAt.toInstant(),
				ZoneId.of("Asia/Taipei")));
		bulletin = bulletinService.modify(bulletin);

		// 更新 LotteryEvents
		existing.setTitle(title);
		existing.setStartedAt(startedAt);
		existing.setEndedAt(endedAt);
		existing.setRentalStart(rentalStart);
		existing.setRentalEnd(rentalEnd);
		existing.setUsers(user);
		existing.setStatus(status);
		existing.setCreatedAt(new Date());
		existing.setParkingType(type);
		repository.save(existing);

		// 刪除舊車位記錄
		lotteryEventSpaceRepository.deleteByLotteryEvents_BulletinId(id);

		// ✅ 去除重複的 parkingSlotId（用 Map 實作去重）
		List<LotteryEventSpaceDTO> distinctParkingSlots = parkingSlots.stream()
				.collect(Collectors.collectingAndThen(Collectors.toMap(LotteryEventSpaceDTO::getParkingSlotId,
						dto -> dto, // value 為原始 DTO
						(a, b) -> a // 若重複，保留第一筆
				), m -> new ArrayList<>(m.values())));

		// 新增車位清單
		List<LotteryEventSpace> newSpaces = distinctParkingSlots.stream().map(slot -> {
			LotteryEventSpace s = new LotteryEventSpace();
			s.setLotteryEvents(existing);
			s.setParkingSlot(parkingSlotRepository.findById(slot.getParkingSlotId()).get());
			return s;
		}).toList();

		List<LotteryEventSpace> savedSpaces = lotteryEventSpaceRepository.saveAll(newSpaces);

		// 組裝回傳 DTO
		LotteryEventUpdateRequest response = new LotteryEventUpdateRequest();
		response.setId(existing.getBulletinId());
		response.setTitle(existing.getTitle());
		response.setTypeId(existing.getParkingType().getId());
		response.setTypeName(existing.getParkingType().getType());
		response.setStatus(existing.getStatus());
		response.setStartedAt(existing.getStartedAt());
		response.setEndedAt(existing.getEndedAt());
		response.setRentalStart(existing.getRentalStart());
		response.setRentalEnd(existing.getRentalEnd());
		response.setUsersId(existing.getUsers().getUsersId());
		response.setCreatedAt(existing.getCreatedAt());

		Set<LotteryEventSpaceDTO> spaceDTOs = savedSpaces.stream().map(space -> {
			LotteryEventSpaceDTO dto = new LotteryEventSpaceDTO();
			dto.setId(space.getId());
			dto.setParkingSlotId(space.getParkingSlot().getId());
			return dto;
		}).collect(Collectors.toSet());

		response.setParkingSlotIds(spaceDTOs);

		return response;
	}

	// 查看參與名單
	public List<LotteryParticipantDTO> getParticipantsByEventId(Integer eventId) {
		List<LotteryApply> applies = lotteryApplyRepository.findByLotteryEvents_BulletinId(eventId);

		return applies.stream().map(apply -> {
			LotteryParticipantDTO dto = new LotteryParticipantDTO();
			dto.setUserName(apply.getUsers().getName());
			dto.setParkingSlots(apply.getLotteryEventSpace().getParkingSlot().getSlotNumber());
			dto.setAppliedAt(apply.getAppliedAt());
			return dto;
		}).toList();
	}

	// 抽籤
	@Transactional
	public DrawLotsResultDTO drawLotsForEvent(Integer eventId) {
		LotteryEvents event = repository.findById(eventId).orElseThrow(() -> new RuntimeException("活動不存在"));

		if (new Date().before(event.getEndedAt())) {
			throw new RuntimeException("活動尚未結束，無法抽籤");
		}

		List<LotteryEventSpace> spaces = lotteryEventSpaceRepository.findByLotteryEvents_BulletinId(eventId);

		List<LotteryApply> applies = lotteryApplyRepository.findByLotteryEvents_BulletinId(eventId);

		if (spaces.isEmpty() || applies.isEmpty()) {
			return new DrawLotsResultDTO(eventId, applies.size(), spaces.size(), new ArrayList<>());
		}

		Collections.shuffle(applies);

		int winnersCount = Math.min(spaces.size(), applies.size());
		List<WinnerDTO> winners = new ArrayList<>();

		for (int i = 0; i < winnersCount; i++) {
			LotteryApply apply = applies.get(i);
			LotteryEventSpace space = spaces.get(i);
			apply.setLotteryEventSpace(space);

			// 可查 slot info（假設有 cache 或 findById）
			Optional<ParkingSlot> slotOp = parkingSlotRepository.findById(space.getParkingSlot().getId());
			String slotNumber = slotOp.map(ParkingSlot::getSlotNumber).orElse(null);

			winners.add(new WinnerDTO(apply.getUsers().getUsersId(), apply.getId(), space.getParkingSlot().getId(),
					slotNumber));
		}

		lotteryApplyRepository.saveAll(applies);

		event.setStatus(true); // 抽籤完成
		repository.save(event);

		return new DrawLotsResultDTO(eventId, applies.size(), spaces.size(), winners);
	}

	// 刪除抽籤活動
	public Boolean delete(Integer id) {
		if (id != null && bulletinEventRepository.existsById(id)) {
			bulletinEventRepository.deleteById(id);

			return true;

		}
		return false;
	}

}