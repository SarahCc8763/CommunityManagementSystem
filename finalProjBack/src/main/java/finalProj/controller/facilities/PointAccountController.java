package finalProj.controller.facilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.facilities.PointAccountsBean;
import finalProj.exception.facilities.ResourceNotFoundException;
import finalProj.service.facilities.PointAccountsService;

@RestController
@RequestMapping("/api/pointAccount")
@CrossOrigin
public class PointAccountController {

	@Autowired
	private PointAccountsService pointAccountsService;

	@GetMapping("/unit/{unitId}")
	public ResponseEntity<PointAccountsBean> getAccountByUnitId(@PathVariable Integer unitId) {
		try {
			PointAccountsBean account = pointAccountsService.findByUnitId(unitId);
			return ResponseEntity.ok(account);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

}
