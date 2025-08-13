package finalProj.controller.facilities;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.facilities.FacilityReservationsBean;
import finalProj.dto.facilities.reservations.ReservationRecordDTO;
import finalProj.service.facilities.FacilityReservationsService;

@RestController
@RequestMapping("/api/facility-reservations")
public class FacilityReservationsController {

	@Autowired
	private FacilityReservationsService facilityReservationsService;

	@GetMapping("/by-facility-and-date")
	public ResponseEntity<List<FacilityReservationsBean>> findByFacilityIdAndDate(
			@RequestParam Integer facilityId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
			LocalDate date) {

		List<FacilityReservationsBean> reservations = facilityReservationsService
				.findByFacilityIdAndDate(facilityId, date);

		return ResponseEntity.ok(reservations);
	}
	
	@GetMapping("/by-unit/{unitId}")
	public ResponseEntity<List<FacilityReservationsBean>> findByUnitId(@PathVariable Integer unitId) {
	    List<FacilityReservationsBean> reservations = facilityReservationsService.findByUnitId(unitId);
	    return ResponseEntity.ok(reservations);
	}
	
	@GetMapping("/by-unit/{unitId}/simple")
	public ResponseEntity<List<ReservationRecordDTO>> getSimpleRecordsByUnitId(@PathVariable Integer unitId) {
	    List<ReservationRecordDTO> records = facilityReservationsService.findRecordsByUnitId(unitId);
	    return ResponseEntity.ok(records);
	}
}
