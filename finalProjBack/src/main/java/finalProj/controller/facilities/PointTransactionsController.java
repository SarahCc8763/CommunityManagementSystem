package finalProj.controller.facilities;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProj.dto.facilities.reservations.PointTransactionsRecordDTO;
import finalProj.service.facilities.PointTransactionsService;

@RestController
@RequestMapping("/api/pointTransactions")
public class PointTransactionsController {
	@Autowired
	private PointTransactionsService pointTransactionsService;

	@GetMapping("/{unitId}")
    public ResponseEntity<List<PointTransactionsRecordDTO>> getTransactionsByUnitId(@PathVariable Integer unitId) {
        try {
            List<PointTransactionsRecordDTO> dtos = pointTransactionsService.findDTOsByUnitId(unitId);
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {        	
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
