package finalProj.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.Bulletin;
import finalProj.domain.LotteryApply;
import finalProj.domain.LotteryEventSpace;
import finalProj.domain.LotteryEvents;
import finalProj.domain.ParkingSlot;
import finalProj.domain.Users;
import finalProj.dto.DrawLotsResultDTO;
import finalProj.dto.LotteryEventSpaceDTO;
import finalProj.dto.LotteryEventUpdateRequest;
import finalProj.dto.WinnerDTO;
import finalProj.repository.BulletinRepository;
import finalProj.repository.LotteryApplyRepository;
import finalProj.repository.LotteryEventRepository;
import finalProj.repository.LotteryEventSpaceRepository;
import finalProj.repository.ParkingSlotRepository;
import finalProj.repository.UsersRepository;
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
	LotteryEventSpaceRepository lotteryEventSpaceRepository;

	@Autowired
	LotteryApplyRepository lotteryApplyRepository;

	@Autowired
	ParkingSlotRepository parkingSlotRepository;

	// 查詢所有抽籤活動
	public List<LotteryEvents> findAll() {
		return repository.findAll();
	}

	// 新增抽籤活動
	public LotteryEventUpdateRequest create(LotteryEventUpdateRequest event) {
		String title = event.getTitle();
		Date startedAt = event.getStartedAt();
		Date endedAt = event.getEndedAt();
		Integer usersId = event.getUsersId();
		Set<LotteryEventSpaceDTO> parkingSlots = event.getParkingSlotIds();

		if (parkingSlots == null || parkingSlots.isEmpty()) {
			return null;
		}

		if (title == null || title.trim().isEmpty()) {
			return null;
		}

		if (startedAt == null || endedAt == null) {
			return null;
		}

		Optional<Users> user = usersRepository.findById(usersId);
		if (user.isEmpty()) {
			return null;
		}

		// 建立 Bulletin
		Bulletin bulletin = new Bulletin();
		bulletin.setTitle(title);
		bulletin.setUser(user.get());
		bulletin.setModifyTime(LocalDateTime.now());
		bulletin.setPostStatus(false);
		bulletin = bulletinEventRepository.save(bulletin);

		// 建立 LotteryEvents 並設定 bulletin 的 ID 為主鍵
		LotteryEvents eventEntity = new LotteryEvents();
		eventEntity.setId(bulletin.getId()); // Bulletin 的 ID = LotteryEvents 的 ID
		eventEntity.setTitle(title);
		eventEntity.setStartedAt(startedAt);
		eventEntity.setEndedAt(endedAt);
		eventEntity.setUsersId(usersId);
		eventEntity.setCreatedAt(new Date());
		repository.save(eventEntity);

		List<LotteryEventSpaceDTO> distinctParkingSlots = parkingSlots.stream()
				.collect(Collectors.collectingAndThen(
						Collectors.toMap(LotteryEventSpaceDTO::getParkingSlotId, dto -> dto, (a, b) -> a),
						m -> new ArrayList<>(m.values())));

		// 新增車位清單
		List<LotteryEventSpace> newSpaces = distinctParkingSlots.stream().map(slot -> {
			LotteryEventSpace s = new LotteryEventSpace();
			s.setLotteryEventsId(eventEntity.getId());
			s.setParkingSlotId(slot.getParkingSlotId());
			return s;
		}).toList();

		List<LotteryEventSpace> savedSpaces = lotteryEventSpaceRepository.saveAll(newSpaces);

		// 組裝回傳 DTO
		LotteryEventUpdateRequest response = new LotteryEventUpdateRequest();
		response.setId(eventEntity.getId());
		response.setTitle(eventEntity.getTitle());
		response.setStartedAt(eventEntity.getStartedAt());
		response.setEndedAt(eventEntity.getEndedAt());
		response.setUsersId(eventEntity.getUsersId());
		response.setCreatedAt(eventEntity.getCreatedAt());

		Set<LotteryEventSpaceDTO> spaceDTOs = savedSpaces.stream().map(space -> {
			LotteryEventSpaceDTO dto = new LotteryEventSpaceDTO();
			dto.setId(space.getId());
			dto.setParkingSlotId(space.getParkingSlotId());
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

		String title = event.getTitle();
		Date startedAt = event.getStartedAt();
		Date endedAt = event.getEndedAt();
		Integer id = event.getId();
		Integer usersId = event.getUsersId();
		Set<LotteryEventSpaceDTO> parkingSlots = event.getParkingSlotIds();

		if (parkingSlots == null || parkingSlots.isEmpty()) {
			return null; // 或 throw new RuntimeException("未選擇車位")
		}

		if (title == null || title.trim().isEmpty()) {
			return null;
		}

		if (startedAt == null || endedAt == null) {
			return null;
		}

		Optional<Users> user = usersRepository.findById(usersId);
		if (user.isEmpty()) {
			return null;
		}

		Optional<Bulletin> bulletinOp = bulletinEventRepository.findById(id);
		if (bulletinOp.isEmpty()) {
			return null;
		}

		// 更新 Bulletin
		Bulletin bulletin = bulletinOp.get();
		bulletin.setTitle(title);
		bulletin.setUser(user.get());
		bulletin.setModifyTime(LocalDateTime.now());
		bulletinEventRepository.save(bulletin);

		// 更新 LotteryEvents
		existing.setTitle(title);
		existing.setStartedAt(startedAt);
		existing.setEndedAt(endedAt);
		existing.setUsersId(usersId);
		repository.save(existing);

		// 刪除舊車位記錄
		lotteryEventSpaceRepository.deleteByLotteryEventsId(id);

		// ✅ 去除重複的 parkingSlotId（用 Map 實作去重）
		List<LotteryEventSpaceDTO> distinctParkingSlots = parkingSlots.stream()
				.collect(Collectors.collectingAndThen(Collectors.toMap(LotteryEventSpaceDTO::getParkingSlotId, // 用
																												// slotId
																												// 當 key
						dto -> dto, // value 為原始 DTO
						(a, b) -> a // 若重複，保留第一筆
				), m -> new ArrayList<>(m.values())));

		// 新增車位清單
		List<LotteryEventSpace> newSpaces = distinctParkingSlots.stream().map(slot -> {
			LotteryEventSpace s = new LotteryEventSpace();
			s.setLotteryEventsId(id);
			s.setParkingSlotId(slot.getParkingSlotId());
			return s;
		}).toList();

		List<LotteryEventSpace> savedSpaces = lotteryEventSpaceRepository.saveAll(newSpaces);

		// 組裝回傳 DTO
		LotteryEventUpdateRequest response = new LotteryEventUpdateRequest();
		response.setId(existing.getId());
		response.setTitle(existing.getTitle());
		response.setStartedAt(existing.getStartedAt());
		response.setEndedAt(existing.getEndedAt());
		response.setUsersId(existing.getUsersId());
		response.setCreatedAt(existing.getCreatedAt());

		Set<LotteryEventSpaceDTO> spaceDTOs = savedSpaces.stream().map(space -> {
			LotteryEventSpaceDTO dto = new LotteryEventSpaceDTO();
			dto.setId(space.getId());
			dto.setParkingSlotId(space.getParkingSlotId());
			return dto;
		}).collect(Collectors.toSet());

		response.setParkingSlotIds(spaceDTOs);

		return response;
	}

	// 抽籤
	@Transactional
	public DrawLotsResultDTO drawLotsForEvent(Integer eventId) {
		LotteryEvents event = repository.findById(eventId).orElseThrow(() -> new RuntimeException("活動不存在"));

		if (new Date().before(event.getEndedAt())) {
			throw new RuntimeException("活動尚未結束，無法抽籤");
		}

		List<LotteryEventSpace> spaces = lotteryEventSpaceRepository.findByLotteryEventsId(eventId);

		List<LotteryApply> applies = lotteryApplyRepository.findByLotteryEventsId(eventId);

		if (spaces.isEmpty() || applies.isEmpty()) {
			return new DrawLotsResultDTO(eventId, applies.size(), spaces.size(), new ArrayList<>());
		}

		Collections.shuffle(applies);

		int winnersCount = Math.min(spaces.size(), applies.size());
		List<WinnerDTO> winners = new ArrayList<>();

		for (int i = 0; i < winnersCount; i++) {
			LotteryApply apply = applies.get(i);
			LotteryEventSpace space = spaces.get(i);
			apply.setLotteryEventSpacesId(space.getId());

			// 可查 slot info（假設有 cache 或 findById）
			Optional<ParkingSlot> slotOp = parkingSlotRepository.findById(space.getParkingSlotId());
			String slotNumber = slotOp.map(ParkingSlot::getSlotNumber).orElse(null);

			winners.add(new WinnerDTO(apply.getUsersId(), apply.getId(), space.getParkingSlotId(), slotNumber));
		}

		lotteryApplyRepository.saveAll(applies);

		return new DrawLotsResultDTO(eventId, applies.size(), spaces.size(), winners);
	}

	// 刪除抽籤活動
	public Boolean delete(Integer id) {
		if (id != null && repository.existsById(id)) {
			repository.deleteById(id);
			return true;
		}
		return false;
	}

}