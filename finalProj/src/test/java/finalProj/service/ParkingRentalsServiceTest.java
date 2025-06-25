package finalProj.service;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import finalProj.domain.ParkingRentals;
import finalProj.util.DatetimeConverter;

@SpringBootTest
public class ParkingRentalsServiceTest {

	@Autowired
	private ParkingRentalsService service;

	@Test
	public void findAll() {
		System.out.println(service.findAll());
	}

	@Test
	void testIsOverlapping_whenOverlapping_thenReturnTrue() {
		Date start = DatetimeConverter.parse("2025-06-24 11:00:00", "yyyy-MM-dd HH:mm:ss");
		Date end = DatetimeConverter.parse("2025-06-24 12:00:00", "yyyy-MM-dd HH:mm:ss");
		Integer slotId = 1;

		System.out.println(service.isOverlapping(start, end, slotId, null));

	}

	@Test
	public void create() {
		ParkingRentals parkingRentals = new ParkingRentals();
		parkingRentals.setUsersId(2);
		parkingRentals.setParkingSlotId(1);
		parkingRentals.setRentBuyStart(DatetimeConverter.parse("2025-06-24 10:00:00", "yyyy-MM-dd HH:mm:ss"));
		parkingRentals.setRentEnd(DatetimeConverter.parse("2025-06-24 11:00:00", "yyyy-MM-dd HH:mm:ss"));
		parkingRentals.setLicensePlate("AAA-123");
		parkingRentals.setStatus(null);

		System.out.println(service.create(parkingRentals));
	}

	@Test
	public void update() {
		ParkingRentals parkingRentals = new ParkingRentals();
		parkingRentals.setId(2);
		parkingRentals.setUsersId(2);
		parkingRentals.setParkingSlotId(2);
		parkingRentals.setRentBuyStart(DatetimeConverter.parse("2021-12-05 12:35:44", "yyyy-MM-dd HH:mm:ss"));
		parkingRentals.setRentEnd(DatetimeConverter.parse("2022-12-05 12:35:44", "yyyy-MM-dd HH:mm:ss"));
		parkingRentals.setLicensePlate("AAA-123");
		parkingRentals.setStatus(true);

		System.out.println(service.update(parkingRentals));
	}

	@Test
	public void delete() {
		System.out.println(service.delete(2));
	}
}
