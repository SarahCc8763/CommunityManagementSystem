package finalProj.service.facilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.facilities.FacilitiesBean;
import finalProj.domain.facilities.FacilityReservationsBean;
import finalProj.dto.facilities.reservations.ReservationRecordDTO;
import finalProj.exception.facilities.InvalidReservationException;
import finalProj.exception.facilities.ResourceNotFoundException;
import finalProj.repository.facilities.FacilityReservationsRepository;

@Service
public class FacilityReservationsServiceImpl implements FacilityReservationsService {
	
	@Autowired
	private FacilityReservationsRepository facilityReservationsRepository;

	@Autowired
	private FacilitiesService facilitiesService;

	@Override
	public FacilityReservationsBean findById(Integer id) {
		return facilityReservationsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("找不到預約 ID: " + id));
	}

	@Override
	public List<FacilityReservationsBean> findByUnitId(Integer unitId) {
		return facilityReservationsRepository.findByUnit_UnitsId(unitId);
	}

	@Override
	public List<FacilityReservationsBean> findByFacilityIdAndDate(Integer facilityId, LocalDate date) {
	    // 設定當天起始與結束時間
	    LocalDateTime startOfDay = date.atStartOfDay();
	    LocalDateTime endOfDay = date.atTime(LocalTime.MAX); // 23:59:59.999999999

	    return facilityReservationsRepository
	        .findByFacilityFacilityIdAndReservationStartTimeBetween(facilityId, startOfDay, endOfDay);
	}


	@Override
	public FacilityReservationsBean create(FacilityReservationsBean reservation) {
		return facilityReservationsRepository.save(reservation);
	}

	@Override
	public FacilityReservationsBean update(FacilityReservationsBean reservation) {
		if (reservation.getReservationId() == null
				|| !facilityReservationsRepository.existsById(reservation.getReservationId())) {
			throw new ResourceNotFoundException("無法更新，找不到預約 ID: " + reservation.getReservationId());
		}
		return facilityReservationsRepository.save(reservation);
	}

	@Override
	public boolean hasConflict(Integer facilityId, LocalDateTime startTime, LocalDateTime endTime, Integer userCount) {
		
		// 查找重疊且狀態為 APPROVED 的預約
		List<FacilityReservationsBean> overlappingReservations =
				facilityReservationsRepository.findOverlappingApprovedReservations(facilityId, startTime, endTime);
		
		FacilitiesBean facility = facilitiesService.findById(facilityId);
		Integer maxUsers = facility.getMaxUsers();
		
		if (maxUsers == null || maxUsers == 0) {
			// 沒有限制，只要有重疊就衝突
			return !overlappingReservations.isEmpty();
		}
		
		// 累計現有重疊預約的人數
		int totalUsers = overlappingReservations.stream()
				.mapToInt(FacilityReservationsBean::getNumberOfUsers)
				.sum();
		
		return (totalUsers + userCount) > maxUsers;
	}
	
	public void validateReservationConflict(Integer facilityId, LocalDateTime startTime, LocalDateTime endTime, Integer userCount) {
	    FacilitiesBean facility = facilitiesService.findById(facilityId);
	    Integer maxUsers = facility.getMaxUsers();

	    if (maxUsers == null || maxUsers == 0) {
	        // 若無上限，檢查是否有任何重疊就拒絕
	        List<FacilityReservationsBean> overlapping =
	            facilityReservationsRepository.findOverlappingApprovedReservations(facilityId, startTime, endTime);

	        if (!overlapping.isEmpty()) {
	            throw new InvalidReservationException("該時段已有其他預約，無法重複預約");
	        }
	        return;
	    }

	    if (userCount == null) {
	        throw new InvalidReservationException("必須輸入使用人數");
	    }

	    // 逐小時檢查是否會超額
	    for (LocalDateTime slotStart = startTime;
	         slotStart.isBefore(endTime);
	         slotStart = slotStart.plusHours(1)) {

	        LocalDateTime slotEnd = slotStart.plusHours(1);

	        List<FacilityReservationsBean> overlapping =
	            facilityReservationsRepository.findOverlappingApprovedReservations(facilityId, slotStart, slotEnd);

	        int alreadyReserved = overlapping.stream()
	            .mapToInt(r -> r.getNumberOfUsers() != null ? r.getNumberOfUsers() : 0)
	            .sum();

	        if (alreadyReserved + userCount > maxUsers) {
	            throw new InvalidReservationException(
	                "預約人數超過上限：時段 " + slotStart.toLocalTime() + " ~ " + slotEnd.toLocalTime() +
	                " 已有 " + alreadyReserved + " 人，本次預約 " + userCount +
	                "，超過上限 " + maxUsers);
	        }
	    }
	}


	@Override
	public List<ReservationRecordDTO> findRecordsByUnitId(Integer unitId) {
	    
	    List<FacilityReservationsBean> reservations = facilityReservationsRepository.findByUnitIdWithFacility(unitId);

	    return reservations.stream().map(res -> {
	        ReservationRecordDTO dto = new ReservationRecordDTO();

	        dto.setReservationId(res.getReservationId());
	        dto.setFacilityName(res.getFacility() != null ? res.getFacility().getFacilityName() : "未知設施");
	        dto.setNumberOfUsers(res.getNumberOfUsers());
	        dto.setReservationStartTime(res.getReservationStartTime());
	        dto.setReservationEndTime(res.getReservationEndTime());
	        dto.setActualUsedPoints(res.getActualUsedPoints());
	        dto.setPointExpireAt(res.getPointExpireAt());
	        dto.setRemark(res.getRemark());
	        dto.setReservationStatus(res.getReservationStatus());
	        dto.setCancelReason(res.getCancelReason());

	        // finalActionTime: 取 createdAt、canceledAt 較晚者
	        LocalDateTime created = res.getCreatedAt();
	        LocalDateTime canceled = res.getCanceledAt();
	        if (created != null && canceled != null) {
	            dto.setFinalActionTime(created.isAfter(canceled) ? created : canceled);
	        } else if (created != null) {
	            dto.setFinalActionTime(created);
	        } else if (canceled != null) {
	            dto.setFinalActionTime(canceled);
	        }

	        return dto;
	    }).collect(Collectors.toList());
	}
}
