package finalProj.controller.community;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.community.Community;
import finalProj.dto.ticket.ApiResponse;
import finalProj.service.community.CommunityService;

@RestController
@RequestMapping("/communitys")
public class CommunityController {

	@Autowired
	CommunityService communityService;

	// Create
	@PostMapping
	public Community create(@RequestBody Community community) {
		// service.save(ticket)
		Community result = communityService.save(community);
		return result;
	}

	// Findall
	@GetMapping
	public List<Community> findAll() {
		// service.findAll()
		return communityService.findAll(); // 空清單回傳
	}

	// ReadbyId
	@GetMapping("/{id}")
	public Community findById(@PathVariable("id") Integer id) {
		// service.findById(id)
		Community result = communityService.findById(id);
		return result;
	}

	// ReadbyCommunityName
	@GetMapping("/name/{name}")
	public ApiResponse<List<Community>> findByCommunityName(@PathVariable("name") String name) {
		List<Community> result = communityService.findByCommunityName(name);
		if (result == null) {
			return new ApiResponse<>(false, "找不到該社區", null);
		} else {
			return new ApiResponse<>(true, "查詢成功", result);
		}
	}

	// Update
	@PutMapping("/{id}")
	public Community update(@PathVariable Integer id, @RequestBody Community community) {
		// service.update(id, ticket)

		Community result = communityService.update(id, community);
		return result;
	}

	// Delete
	@DeleteMapping("/{id}")
	public boolean delete(@PathVariable("id") Integer id) {
		// service.delete(id)
		boolean result = communityService.remove(id);
		return result;
	}

}
