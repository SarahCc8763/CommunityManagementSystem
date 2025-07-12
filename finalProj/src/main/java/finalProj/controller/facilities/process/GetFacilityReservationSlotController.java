package finalProj.controller.facilities.process;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProj.dto.facilities.reservations.AvailableReservationSlotResponse;
import finalProj.service.facilities.process.reservationSlot.ShowAvailableReservationSlotService;
@CrossOrigin
@RestController
@RequestMapping("/api/facility-reservation")
public class GetFacilityReservationSlotController {

	@Autowired
	private ShowAvailableReservationSlotService showAvailableReservationSlotService;

	@GetMapping("/available-slots/{id}")
	public ResponseEntity<List<AvailableReservationSlotResponse>> getAvailableSlots(
			@PathVariable("id") Integer facilityId) {
		List<AvailableReservationSlotResponse> slots = showAvailableReservationSlotService
				.getAvailableSlots(facilityId);
		return ResponseEntity.ok(slots);
	}
}
