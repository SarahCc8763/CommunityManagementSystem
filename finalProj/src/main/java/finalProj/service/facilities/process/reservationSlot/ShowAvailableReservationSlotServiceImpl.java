package finalProj.service.facilities.process.reservationSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.facilities.FacilitiesBean;
import finalProj.domain.facilities.FacilityReservationsBean;
import finalProj.dto.facilities.reservations.AvailableReservationSlotResponse;
import finalProj.exception.facilities.ResourceNotFoundException;
import finalProj.repository.facilities.FacilitiesRepository;
import finalProj.repository.facilities.FacilityReservationsRepository;

@Service
public class ShowAvailableReservationSlotServiceImpl implements ShowAvailableReservationSlotService {

    @Autowired
    private FacilitiesRepository facilitiesRepository;

    @Autowired
    private FacilityReservationsRepository reservationsRepository;

    @Override
    public List<AvailableReservationSlotResponse> getAvailableSlots(Integer facilityId) {
		
    	//判斷設施是否存在且active
    	FacilitiesBean facility = facilitiesRepository.findById(facilityId)
				.orElseThrow(() -> new ResourceNotFoundException("找不到公設"));

		if (!"active".equalsIgnoreCase(facility.getFacilityStatus())) {
            throw new ResourceNotFoundException("公設狀態非啟用中，無法進行操作");
        }
		
		LocalTime openTime = facility.getOpenTime();
        LocalTime closeTime = facility.getCloseTime();
        Integer maxUsers = facility.getMaxUsers();
        
        // 取得今天的日期
        LocalDate today = LocalDate.now();
        // 定義查詢範圍：過去 7 天 ～ 未來 14 天
        LocalDate fromDate = today.minusDays(7);//7
        LocalDate toDate = today.plusDays(14);	//14

        // 組合成 LocalDateTime
        LocalDateTime startDateTime = fromDate.atTime(openTime);
        LocalDateTime endDateTime = toDate.atTime(closeTime);
        
        //查詢該設施時段內預約
        List<FacilityReservationsBean> reservations = reservationsRepository
                .findByFacilityFacilityIdAndReservationStartTimeBetweenAndReservationStatus
                (facilityId, startDateTime, endDateTime, "APPROVED");
                

        //建立slots表
        List<AvailableReservationSlotResponse> slots = new ArrayList<>();
        
        LocalDateTime nowPlusOneHour = LocalDateTime.now().plusHours(1);

		for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {

			int for24HRFacilityCount = 0;

			for (LocalTime time = openTime; time.plusMinutes(60).compareTo(closeTime) <= 0; time = time
					.plusMinutes(60)) {            	
            	
            	LocalDateTime slotStart = date.atTime(time);
                LocalDateTime slotEnd = slotStart.plusMinutes(60);              
                
                boolean hasReservation = reservations.stream()
                	    .anyMatch(r -> isSlotCoveredByReservation(slotStart, r.getReservationStartTime(), r.getReservationEndTime()));

                	int reservedUsers = reservations.stream()
                	    .filter(r -> isSlotCoveredByReservation(slotStart, r.getReservationStartTime(), r.getReservationEndTime()))
                	    .mapToInt(r -> r.getNumberOfUsers() == null ? 0 : r.getNumberOfUsers())
                	    .sum();
                	
                
                boolean isAvailable = true;               

                if (slotStart.isBefore(nowPlusOneHour)) {						// 條件：過去時間（比現在 + 1hr 小）不可預約
                    isAvailable = false;                    
                } else if (maxUsers == null && hasReservation) {				// 條件：沒有限制人數，且有人預約 → 不可預約                	
                    isAvailable = false;
                }else if (maxUsers != null && reservedUsers >= maxUsers) {     	// 條件：有最大人數，且已滿 → 不可預約
                    isAvailable = false;
                }
                
				slots.add(new AvailableReservationSlotResponse(date, time, maxUsers, reservedUsers, isAvailable));

				if (closeTime.equals(LocalTime.of(23, 59))) {
					for24HRFacilityCount++;
				}
				if (for24HRFacilityCount == 24) {
					break;
				}
            }
        }

        return slots;
    }
    
    private boolean isSlotCoveredByReservation(LocalDateTime slotStart, LocalDateTime resStart, LocalDateTime resEnd) {
        LocalDateTime slotEnd = slotStart.plusMinutes(60);
        return resStart.isBefore(slotEnd) && resEnd.isAfter(slotStart);
    }
}






























