package finalProj.service.packages;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import finalProj.domain.community.Community;
import finalProj.domain.packages.Packages;
import finalProj.domain.users.Units;
import finalProj.dto.packages.PackagesDTO;
import finalProj.dto.packages.PackagesSearchDTO;
import finalProj.enumCommunity.CommunityFunction;
import finalProj.repository.community.CommunityRepository;
import finalProj.repository.notifications.NotificationsRepository;
import finalProj.repository.notifications.UnitsNotificationsRepository;
import finalProj.repository.packages.PackagesRepository;
import finalProj.repository.users.UnitsRepository;
import finalProj.service.notifications.NotificationsService;
import finalProj.util.CommunityFunctionUtils;

@Service
@Transactional
public class PackagesService {

	@Autowired
	private PackagesRepository PackagesRepository;

	@Autowired
	private CommunityRepository communityRepository;

	@Autowired
	private UnitsRepository unitsRepository;

	@Autowired
	private NotificationsRepository notificationsRepository;

	@Autowired
	private UnitsNotificationsRepository unitsNotificationsRepository;

	// @Autowired
	// private UsersNotificationsRepository usersNotificationsRepository;

	@Autowired
	private NotificationsService notificationsService;

	// @Autowired
	// private NotificationsWebSocketService notificationsWebSocketService;

	// 新增包裹
	public Packages addPackages(Map<String, String> body) {

		// 拿 communityId 並轉成 Integer
		Integer communityId = Integer.parseInt(body.get("communityId"));
		Community community = communityRepository.findById(communityId)
				.orElseThrow(() -> new IllegalArgumentException("社區 ID 不存在：" + communityId));

		if (!CommunityFunctionUtils.hasFunction(community.getFunction(), CommunityFunction.PACKAGE)) {
			throw new IllegalArgumentException("此社區未啟用『包裹』功能，無法建立包裹紀錄");
		}

		// 組合 unit + floor
		String unit = body.get("unit"); // 前端組合好的 "10-1"
		String floor = body.get("floor"); // 例如 "3F"

		Units foundUnit = unitsRepository.findByUnitAndFloor(unit, floor)
				.orElseThrow(() -> new IllegalArgumentException("找不到對應的門牌，unit=" + unit + ", floor=" + floor));

		// 種類
		String type = body.get("type");

		// 件數
		String pieceStr = body.get("piece");
		int piece = pieceStr != null ? Integer.parseInt(pieceStr) : 1;

		// 地點
		String place = body.get("place");

		// 狀態
		String status = body.get("status");

		Packages packages = new Packages();
		packages.setCommunity(community);
		packages.setUnit(foundUnit);
		packages.setPiece(piece);
		packages.setArrivalTime(LocalDateTime.now());
		packages.setPickupTime(null);
		if (status == null || status.isBlank()) {
			packages.setStatus("未領取");
		}
		packages.setType(type);
		packages.setSign(null);
		packages.setPlace(place);

		packages.setPhoto(null);

		Packages savedPackage = PackagesRepository.save(packages);

		notificationsService.createPackageNotification(savedPackage);

		return savedPackage;
	}
	// public Packages addPackages(PackagesDTO dto) {
	// Community community = communityRepository.findById(dto.getCommunityId())
	// .orElseThrow(() -> new IllegalArgumentException("社區 ID 不存在：" +
	// dto.getCommunityId()));
	//
	// if (!CommunityFunctionUtils.hasFunction(community.getFunction(),
	// CommunityFunction.PACKAGE)) {
	// throw new IllegalArgumentException("此社區未啟用『包裹』功能，無法建立包裹紀錄");
	// }
	//
	// Units unit = unitsRepository.findById(dto.getUnitId())
	// .orElseThrow(() -> new IllegalArgumentException("門牌 ID 不存在：" +
	// dto.getUnitId()));
	//
	// Packages packages = new Packages();
	// packages.setCommunity(community);
	// packages.setUnit(unit);
	// packages.setPiece(dto.getPiece());
	// packages.setArrivalTime(LocalDateTime.now());
	// // ??????????
	// packages.setPickupTime(dto.getPickupTime());
	// if (dto.getStatus() == null || dto.getStatus().isBlank()) {
	// packages.setStatus("未領取");
	// } else {
	// packages.setStatus(dto.getStatus());
	// }
	// packages.setType(dto.getType());
	// packages.setSign(dto.getSign());
	// packages.setPlace(dto.getPlace());
	//
	// if (dto.getPhoto() != null) {
	// packages.setPhoto(dto.getPhoto());
	// }
	//
	// Packages savedPackage = PackagesRepository.save(packages);
	//
	// notificationsService.createPackageNotification(savedPackage);
	//
	// return savedPackage;
	// }

	// 查詢包裹
	public List<Packages> findPackagesByUnitId(Integer unitId) {
		Units unit = unitsRepository.findById(unitId)
				.orElseThrow(() -> new IllegalArgumentException("找不到該 unitId：" + unitId));

		return PackagesRepository.findByUnit(unit);
	}

	// 查詢包裹
	public List<Packages> findPackagesByUnitIdSecurity(String unit, String floor) {
		Optional<Units> units = unitsRepository.findByUnitAndFloor(unit, floor);
		Units foundUnits = units.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "找不到 units"));
		// Units foundUnits = units.orElseThrow(() -> new RuntimeException("找不到
		// units"));
		// if (units.isEmpty()) {
		// System.out.println("找不到 units");
		// return Collections.emptyList();
		// }
		//
		// Units foundUnits = units.get();
		return PackagesRepository.findByUnit(foundUnits);
	}

	// 多條件查詢
	public List<Packages> search(PackagesSearchDTO dto, Integer unitId) {
		return PackagesRepository.searchByConditions(dto.getStatus(), dto.getType(), dto.getArrivalTime(), unitId);
	}

	// 改變狀態
	public void markAsPickedUp(Integer packagesId) {
		PackagesRepository.updateStatusById(packagesId, "已領取");
	}

	// 修改
	public Packages update(Integer id, PackagesDTO dto) {
		Packages packages = PackagesRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("找不到對應的包裹 ID：" + id));
		;

		if (dto.getCommunityId() != null) {
			Community community = communityRepository.findById(dto.getCommunityId())
					.orElseThrow(() -> new IllegalArgumentException("找不到對應的 communityId：" + dto.getCommunityId()));
			packages.setCommunity(community);
		}
		if (dto.getUnitId() != null) {
			Units unit = unitsRepository.findById(dto.getUnitId())
					.orElseThrow(() -> new IllegalArgumentException("找不到對應的 unitId：" + dto.getUnitId()));
			packages.setUnit(unit);
		}
		packages.setPiece(dto.getPiece());
		packages.setType(dto.getType());
		packages.setPlace(dto.getPlace());

		return PackagesRepository.save(packages);
	}

}
