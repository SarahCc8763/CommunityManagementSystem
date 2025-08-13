package finalProj.service.facilities.process.cancelReservationWithPointRefound;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.facilities.FacilityReservationsBean;
import finalProj.exception.facilities.InvalidReservationException;
import finalProj.exception.facilities.ResourceNotFoundException;
import finalProj.repository.facilities.FacilityReservationsRepository;

@Service
public class CancelReservationValidator {

    @Autowired
    private FacilityReservationsRepository facilityReservationsRepository;

    /**
     * 驗證是否可以取消預約，並回傳 Reservation 物件
     */
    public FacilityReservationsBean validate(Integer reservationId) {
        FacilityReservationsBean reservation = facilityReservationsRepository.findById(reservationId)
            .orElseThrow(() -> new ResourceNotFoundException("找不到指定的預約紀錄"));

        // 是否已取消
        if ("CANCELLED".equalsIgnoreCase(reservation.getReservationStatus())) {
            throw new InvalidReservationException("此預約已被取消，無法再次取消");
        }

        // 是否已過可取消時間（如：開始前一小時）
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = reservation.getReservationStartTime();
        
        // 現在時間的下一個整點
        LocalDateTime nextFullHour = now.withMinute(0).withSecond(0).withNano(0);
        if (now.getMinute() > 0 || now.getSecond() > 0 || now.getNano() > 0) {
            nextFullHour = nextFullHour.plusHours(1);
        }

        // 若預約開始時間 <= 下一整點，則不能取消
        if (!startTime.isAfter(nextFullHour)) {
        	System.out.println("Now: " + now);
        	System.out.println("NextFullHour: " + nextFullHour);
        	System.out.println("StartTime: " + startTime);
        	System.out.println("CanCancel: " + startTime.isAfter(nextFullHour));
            throw new InvalidReservationException("預約已接近開始時間，無法取消");
        }
        return reservation;
    }
}
