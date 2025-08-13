
package finalProj.service.users;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import finalProj.domain.users.Users;
import finalProj.repository.users.UsersRepository;

@SpringBootTest
public class UsersServiceTests {

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private UsersService usersService;

	@Test
	public void isValidUser() {

		System.out.println("isValidUser( )測試結果1應為true : " + usersService.isValidUser("safe@example.com"));
		System.out.println("isValidUser( )測試結果2應為false : " + usersService.isValidUser("xiao@example.com"));

	}

	@Test
	public void setPasswordTest() {
		usersService.resetPassword("xiao@example.com", "3333");
		Optional<Users> optionalUser = usersRepository.findByEmail("daming@example.com");
		if (optionalUser.isPresent()) {
			Users bean = optionalUser.get();
			System.out.println(
					"setPasswordTest() finished. Password set. Encoded password is : " + bean.getPassword().toString());
		}
	}

	@Test
	public void firstTimeChangePasswordTest() {
		System.out.println("firstTimeChangePasswordTest()測試結果1應為 false : "
				+ usersService.firstTimeChangePassword("fang@example.com", "1234", "2222"));
		System.out.println("firstTimeChangePasswordTest()測試結果2應為 true: "
				+ usersService.firstTimeChangePassword("fang@example.com", "P@ssw0rd", "2222"));
	}

	@Test
	public void loginTest() {

		System.out.println("loginTest()測試結果1應為null : " + usersService.login("daming@example.com", "0000"));
		System.out.println("loginTest()測試結果2應為User : " + usersService.login("daming@example.com", "1111"));
		System.out.println("loginTest()測試結果3應為User : " + usersService.login("fang@example.com", "2222"));

	}

	// @Test
	// public void changePasswordTest() {
	//
	// System.out.println(
	// "changePasswordTest()測試結果 : " +
	// usersService.changePassword("safe@example.com", "P@ssw0rd", "0000"));
	// System.out.println("loginTest()測試結果2應為null : " +
	// usersService.login("xiao@example.com","P@ssw0rd"));
	//
	// }

}
