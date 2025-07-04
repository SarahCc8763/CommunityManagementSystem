package finalProj.controller.ticket;

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

import finalProj.domain.ticket.IssueType;
import finalProj.service.ticket.IssueTypeService;

@RestController
@RequestMapping("/IssueTypes")
public class IssueTypeController {
	@Autowired
	IssueTypeService issueTypeService;

	// Create
	@PostMapping
	public IssueType create(@RequestBody IssueType issueType) {
		// 呼叫 service.save(ticket)
		IssueType result = issueTypeService.save(issueType);
		return result;
	}

	// Findall
	@GetMapping
	public List<IssueType> findAll() {
		// 呼叫 service.findAll()
		return issueTypeService.findAll(); // 空清單回傳
	}

	// Read by id
	@GetMapping("/{id}")
	public IssueType findById(@PathVariable Integer id) {
		// 呼叫 service.findById(id)
		IssueType result = issueTypeService.findById(id);
		return result;
	}

	// // Update
	@PutMapping("/{id}")
	public IssueType update(@PathVariable Integer id, @RequestBody IssueType issueType) {
		// 呼叫 service.update(id, ticket)

		IssueType result = issueTypeService.update(id, issueType);
		return result;
	}

	// // Delete
	@DeleteMapping("/{id}")
	public boolean delete(@PathVariable Integer id) {
		// 呼叫 service.delete(id)
		boolean result = issueTypeService.remove(id);
		return result;
	}
}
