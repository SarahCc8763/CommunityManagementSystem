//package finalProj.service.parking;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import finalProj.domain.parking.ParkingType;
//import finalProj.service.parking.ParkingTypeService;
//
//@SpringBootTest
//public class ParkingTypeServiceTest {
//
//	@Autowired
//	private ParkingTypeService service;
//
//	@Test
//	public void findAll() {
//		System.out.println(service.findAll());
//	}
//
//	@Test
//	public void create() {
//		ParkingType parkingType = new ParkingType();
//		parkingType.setCommunityId(null);
//		parkingType.setType("殘障車位");
//		System.out.println(service.create(parkingType));
//	}
//
//	@Test
//	public void update() {
//		ParkingType parkingType = new ParkingType();
//		parkingType.setId(4);
//		parkingType.setCommunityId(null);
//		parkingType.setType("殘障車位u");
//		System.out.println(service.update(parkingType));
//	}
//	
//	@Test
//	public void delete() {
//		System.out.println(service.delete(4));
//	}
//}
