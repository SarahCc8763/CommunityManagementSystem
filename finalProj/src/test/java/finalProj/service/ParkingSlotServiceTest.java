package finalProj.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import finalProj.domain.ParkingSlot;

@SpringBootTest
public class ParkingSlotServiceTest {

	@Autowired
	private ParkingSlotService service;

	@Test
	public void findAll() {
		System.out.println(service.findAll());
	}

	@Test
	public void create() {
		ParkingSlot parkingSlot = new ParkingSlot();
		parkingSlot.setCommunityId(null);
		parkingSlot.setSlotNumber("B1-C01");
		parkingSlot.setLocation("B1層C排");
		parkingSlot.setParkingTypeId(2);
		parkingSlot.setUsersId(1);
		parkingSlot.setLicensePlate("AAA-123");
		parkingSlot.setIsRentable(null);

		System.out.println(service.create(parkingSlot));
	}

	@Test
	public void update() {
		ParkingSlot parkingSlot = new ParkingSlot();
		parkingSlot.setId(3);
		parkingSlot.setCommunityId(null);
		parkingSlot.setSlotNumber("B1-C01");
		parkingSlot.setLocation("B1層C排");
		parkingSlot.setParkingTypeId(2);
		parkingSlot.setUsersId(1);
		parkingSlot.setLicensePlate("AAA-123");
		parkingSlot.setIsRentable(true);

		System.out.println(service.update(parkingSlot));
	}
	
	@Test
	public void delete() {
		System.out.println(service.delete(3));
	}
}
