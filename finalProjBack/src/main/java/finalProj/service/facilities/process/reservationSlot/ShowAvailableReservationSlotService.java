package finalProj.service.facilities.process.reservationSlot;

import java.util.List;

import finalProj.dto.facilities.reservations.AvailableReservationSlotResponse;

public interface ShowAvailableReservationSlotService {

	List<AvailableReservationSlotResponse> getAvailableSlots(Integer facilityId);

}
