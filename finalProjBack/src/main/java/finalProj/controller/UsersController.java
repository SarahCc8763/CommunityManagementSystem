package finalProj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.users.Users;
import finalProj.dto.parking.ApiResponse;
import finalProj.repository.UsersRepository;

@RestController
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private UsersRepository usersRepository;

	@GetMapping
	public ResponseEntity<ApiResponse<List<Users>>> findByCommunity(@RequestParam("communityId") Integer communityId) {
		List<Users> users = usersRepository.findByCommunity_CommunityId(communityId);
		return ResponseEntity.ok(ApiResponse.success("查詢成功", users));
	}
}
