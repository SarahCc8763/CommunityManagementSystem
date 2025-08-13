package finalProj.controller.facilities.process;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProj.dto.facilities.topup.TopupRequest;
import finalProj.service.facilities.process.pointTopup.PointTopupService;
@CrossOrigin
@RestController
@RequestMapping("/api/point-topup")
public class PointTopupController {

	@Autowired
	private PointTopupService pointTopupService;

	@PostMapping("/create-order")
	public ResponseEntity<Map<String, String>> createOrder(@RequestBody TopupRequest request) {
	    try {
	        String htmlForm = pointTopupService.generateEcpayForm(request);
	        return ResponseEntity.ok(Map.of("form", htmlForm));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of("error", "產生綠界付款表單失敗", "message", e.getMessage()));
	    }
	}
}
