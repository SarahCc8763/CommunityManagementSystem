package finalProj.controller.facilities.process;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProj.dto.facilities.reservations.CancelReservationRequest;
import finalProj.dto.facilities.reservations.ReservationRequest;
import finalProj.service.facilities.process.cancelReservationWithPointRefound.CancelReservationWithPointRefoundService;
import finalProj.service.facilities.process.reserveWithPointDeduction.ReservationProcessingService;
@CrossOrigin
@RestController
@RequestMapping("/api/facility-reservation")
public class ReservationProcessingController {

    @Autowired
    private ReservationProcessingService reservationProcessingService;
    
    @Autowired
    private CancelReservationWithPointRefoundService cancelReservationWithPointRefoundService;
    

    // 預約並扣點
    @PostMapping("/reserve")
    public ResponseEntity<Map<String, String>> reserve(@RequestBody ReservationRequest request) {
        try {
            reservationProcessingService.reserveWithPointDeduction(request);            
            return ResponseEntity.ok(Map.of("message", "預約成功"));
        } catch (Exception e) {        	
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    // 取消預約並退點
    @PostMapping("/cancel")    
    public ResponseEntity<String> cancel(@RequestBody CancelReservationRequest request) {
        try {
        	cancelReservationWithPointRefoundService.cancelReservationAndRefund(request);
            return ResponseEntity.ok("取消預約並退回點數成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("取消失敗：" + e.getMessage());
        }
    }


}
