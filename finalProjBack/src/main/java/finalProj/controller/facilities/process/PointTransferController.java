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

import finalProj.dto.facilities.transfer.PointTransferRequest;
import finalProj.exception.facilities.ResourceNotFoundException;
import finalProj.service.facilities.process.pointTransfer.PointTransferService;
@CrossOrigin
@RestController
@RequestMapping("/api/point-transfer")
public class PointTransferController {

	@Autowired
	private PointTransferService pointTransferService;

	@PostMapping
    public ResponseEntity<?> transferPoints(@RequestBody PointTransferRequest request) {
        try {
            pointTransferService.transfer(request); // ← 執行轉移邏輯            
            return ResponseEntity.ok(Map.of("message", "轉移成功"));         		
        } catch (IllegalArgumentException | IllegalStateException e) {
            // 使用者輸入錯誤或邏輯問題
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (ResourceNotFoundException e) {
            // 找不到帳戶或單位等
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // 其他未知錯誤        	
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }
    }
}
