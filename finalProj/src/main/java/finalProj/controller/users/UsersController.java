package finalProj.controller.users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.users.Units;
import finalProj.domain.users.Users;
import finalProj.dto.parking.ApiResponse;
import finalProj.jwt.JsonWebTokenUtility;
import finalProj.repository.users.UsersRepository;
import finalProj.service.users.UsersService;

//@CrossOrigin
@RestController
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private UsersService usersService;

	@Autowired
	private JsonWebTokenUtility jwtUtility;

	@PostMapping("/resetPassword")
	public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
		String email = body.get("email");
		String password = body.get("newPassword");
		if (usersService.isValidUser(email)) {
			usersService.resetPassword(email, password);
			return ResponseEntity.ok(Map.of("message", "密碼更改成功", "success", "true"));
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
			// 判斷有無值
			Units unitObj = null;
			if (user.getUnitsUsersList() != null
					&& !user.getUnitsUsersList().isEmpty()
					&& user.getUnitsUsersList().getFirst().getUnit() != null) {
				unitObj = user.getUnitsUsersList().getFirst().getUnit();
			}

			Map<String, Object> response = new HashMap<>();
			response.put("success", "true");
			response.put("message", "登入成功");
			response.put("token", token);
			response.put("email", user.getEmail());
			response.put("id", user.getUsersId());
			response.put("name", user.getName());
			response.put("roleId", user.getRolesUsersList().getFirst().getRole().getRolesId());
			response.put("communityId", user.getCommunity().getCommunityId());
			response.put("state", user.getState());
			response.put("contactInfo", user.getContactInfo());
			response.put("emergencyContactName", user.getEmergencyContactName());
			response.put("emergencyContactRelation", user.getEmergencyContactRelation());
			response.put("emergencyContactPhone", user.getEmergencyContactPhone());
			response.put("photo", user.getPhoto() != null ? user.getPhoto() : null);
			response.put("logFaildTimes", user.getLoginFailTimes());
			response.put("points", unitObj != null ? unitObj.getPoint() : null);
			response.put("unitId", unitObj != null ? unitObj.getUnitsId() : null);
			response.put("unit", unitObj != null ? unitObj.getUnit() : null);
			response.put("floor", unitObj != null ? unitObj.getFloor() : null);
			return ResponseEntity.ok(response);

			// ResponseEntity.ok(Map.of(
			// "success","true",
			// "message", "登入成功",
			// "token", token,
			// "email", user.getEmail(),
			// "id",
			// user.getUsersId(),
			// "name", user.getName(),
			// "communityId", user.getCommunity().getCommunityId(),
			// "photo", user.getPhoto(),
			// "logFaildTimes", user.getLoginFailTimes(),
			// "points", user.getUnitsUsersList().get(0).getUnit().getPoint(),
			// "unit",user.getUnitsUsersList().get(0).getUnit().getUnit()// 你可以加上更多資料
			// ));
			// 登入失敗
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "登入失敗"));
		}
	}

	@GetMapping("/ticket")
	public List<Users> findAll() {
		// 呼叫 service.findAll()
		return usersService.findAll(); // 空清單回傳
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<Users>>> findByCommunity(@RequestParam("communityId") Integer communityId) {
		List<Users> users = usersRepository.findByCommunity_CommunityId(communityId);
		return ResponseEntity.ok(ApiResponse.success("查詢成功", users));
	}
}