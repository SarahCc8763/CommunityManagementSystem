package finalProj.controller.finance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.finance.Receipt;
import finalProj.dto.finance.ReceiptDTO;
import finalProj.service.finance.baseServiceInterfaces.ReceiptService;

@RestController
@RequestMapping("/finance/receipts")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    // 【功能】建立收據（繳費後由系統產生）
    @PostMapping
    public ResponseEntity<ReceiptDTO> createReceipt(@RequestBody ReceiptDTO dto) {

        try {
            ReceiptDTO saved = receiptService.createReceipt(dto);

            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // 或可客製錯誤訊息
        }
    }

    // 【功能】查詢所有收據（可依communityId查詢）
    @GetMapping
    public ResponseEntity<List<Receipt>> getAllReceipts(@RequestParam(required = false) Integer communityId) {
        if (communityId != null) {
            return ResponseEntity.ok(receiptService.findByCommunityId(communityId));
        } else {
            return ResponseEntity.ok(receiptService.findAll());
        }
    }

    // 【功能】查詢單一收據明細
    @GetMapping("/{id}")
    public ResponseEntity<Receipt> getReceiptById(@PathVariable Integer id) {
        return ResponseEntity.ok(receiptService.findById(id));
    }

    // 【功能】更新收據內容
    @PutMapping("/{id}")
    public ResponseEntity<Receipt> updateReceipt(@PathVariable Integer id, @RequestBody Receipt receipt) {
        receipt.setReceiptId(id);
        return ResponseEntity.ok(receiptService.save(receipt));
    }

    // 【功能】刪除收據
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReceipt(@PathVariable Integer id) {
        receiptService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // 依用戶ID查詢收據
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Receipt>> getReceiptsByUserId(@PathVariable Integer userId) {
        List<Receipt> all = receiptService.findAll();
        List<Receipt> filtered = all.stream()
                .filter(r -> r.getUsers() != null && r.getUsers().getUsersId().equals(userId)).toList();
        return ResponseEntity.ok(filtered);
    }
}
