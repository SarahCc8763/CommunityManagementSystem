package finalProj.controller.users;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.users.Users;
import finalProj.jwt.JsonWebTokenUtility;
import finalProj.service.users.UsersService;
//@CrossOrigin
@RestController
@RequestMapping("/users")
public class UsersController {

//	@Autowired
//	private UsersRepository usersRepository;

	@Autowired
	private UsersService usersService;

	@Autowired
	private JsonWebTokenUtility jwtUtility;

	@PostMapping("/resetPassword")
	public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
		String email = body.get("email");
		String password = body.get("newPassword");
		if(usersService.isValidUser(email)) {
		usersService.resetPassword(email, password);
		return ResponseEntity.ok(Map.of("message", "密碼更改成功"));
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "密碼更改失敗"));
	}

	@PostMapping("/firstTimeChangePassword")
	public ResponseEntity<?> firstTimeChangePassword(@RequestBody Map<String, String> body) {
		String email = body.get("email");
		String oldPass = body.get("oldPassword");
		String newPass = body.get("newPassword");

		// 驗證是否為首次登入
		if (usersService.firstTimeChangePassword(email, oldPass, newPass)) {
			// 密碼修改成功
			return ResponseEntity.ok(Map.of("message", "更改成功"));
		}
		// 密碼修改失敗
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "更改失敗"));
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
		String email = body.get("email");
		String password = body.get("password");
		// 嘗試登入
		Users user = usersService.login(email, password);
		// 登入成功
		if (user != null) {
			String token = jwtUtility.createToken(user.getEmail()); // 你現在的 JWT 只用 email
			return ResponseEntity.ok(Map.of("success","true","message", "登入成功", "token", token, "email", user.getEmail(), "id",
					user.getUsersId(), "name", user.getName(), "communityId", user.getCommunity().getCommunityId(), "photo",
					user.getPhoto(), "logFaildTimes", user.getLoginFailTimes()// 你可以加上更多資料
			));
			// 登入失敗
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "登入失敗"));
		}
	}

}
