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

import finalProj.domain.ticket.Vendor;
import finalProj.service.ticket.VendorService;

@RestController
@RequestMapping("/vendors")
public class VendorController {
	@Autowired
	private VendorService vendorService;

	// Create
	@PostMapping
	public Vendor create(@RequestBody Vendor vendor) {
		// TODO: 呼叫 service.save(ticket)
		Vendor result = vendorService.save(vendor);
		return result;
	}

	// Findall
	@GetMapping
	public List<Vendor> findAll() {
		// TODO: 呼叫 service.findAll()
		return vendorService.findAll(); // 空清單回傳
	}

	// // Read by id
	@GetMapping("/{id}")
	public Vendor findById(@PathVariable Integer id) {
		// TODO: 呼叫 service.findById(id)
		Vendor result = vendorService.findById(id);
		return result;
	}

	// // Update
	@PutMapping("/{id}")
	public Vendor update(@PathVariable Integer id, @RequestBody Vendor vendor) {
		// TODO: 呼叫 service.update(id, ticket)

		Vendor result = vendorService.update(id, vendor);
		return result;
	}

	// // Delete
	@DeleteMapping("/{id}")
	public boolean delete(@PathVariable Integer id) {
		// TODO: 呼叫 service.delete(id)
		boolean result = vendorService.remove(id);
		return result;
	}
}
