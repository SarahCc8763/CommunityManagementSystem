package finalProj.controller.finance;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import finalProj.domain.finance.Invoice;
import finalProj.dto.finance.InvoiceDTO;
import finalProj.service.finance.baseServiceInterfaces.InvoiceService;

@RestController
@RequestMapping("/finance/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    // 查詢登入者未繳帳單（傳 userId）
    @PostMapping("/unpaid/by-user")
    public ResponseEntity<List<InvoiceDTO>> getUnpaidInvoicesByUser(@RequestBody Map<String, Integer> payload) {
        Integer userId = payload.get("userId");
        return ResponseEntity.ok(invoiceService.findUnpaidInvoicesByUserId(userId));
    }

    // 查詢社區內所有未繳帳單（傳 communityId）
    @PostMapping("/unpaid/by-community")
    public ResponseEntity<List<InvoiceDTO>> getUnpaidInvoicesByCommunity(@RequestBody Map<String, Integer> payload) {
        Integer communityId = payload.get("communityId");
        return ResponseEntity.ok(invoiceService.findUnpaidInvoicesByCommunityId(communityId));
    }

    // 依ID查詢單一發票
    @GetMapping("/getOne/{invoiceId}")
    public ResponseEntity<Invoice> getById(@PathVariable Integer invoiceId) {
        return ResponseEntity.ok(invoiceService.findById(invoiceId));
    }

    // 建立新請款
    @PostMapping
    public ResponseEntity<Invoice> create(@RequestBody Invoice invoice) {
        return ResponseEntity.ok(invoiceService.save(invoice));
    }

    // 更新請款內容
    @PutMapping("/updateOne/{invoiceId}")
    public ResponseEntity<Invoice> update(@PathVariable Integer invoiceId, @RequestBody Invoice updated) {
        updated.setInvoiceId(invoiceId);
        return ResponseEntity.ok(invoiceService.save(updated));
    }

    // 刪除請款
    @DeleteMapping("/{invoiceId}")
    public ResponseEntity<Void> delete(@PathVariable Integer invoiceId) {
        invoiceService.deleteById(invoiceId);
        return ResponseEntity.noContent().build();
    }

    // 依期別代碼產生/查詢該期所有請款
    @GetMapping("/generate/{periodCode}")
    public ResponseEntity<List<Invoice>> generateByPeriod(@PathVariable String periodCode) {
        return ResponseEntity.ok(invoiceService.generateInvoicesForPeriod(periodCode));
    }

    // 更新請款狀態（如已繳/未繳/審核中）
    @PutMapping("/status/{invoiceId}")
    public ResponseEntity<Invoice> updateStatus(@PathVariable Integer invoiceId, @RequestParam String status) {
        Invoice invoice = invoiceService.findById(invoiceId);
        invoice.setPaymentStatus(status);
        return ResponseEntity.ok(invoiceService.save(invoice));
    }

    // 取得單位數
    @GetMapping("/unit-count")
    public ResponseEntity<java.math.BigDecimal> getUnitCount(
            @RequestParam Integer usersId, @RequestParam Integer feeTypeId) {
        return ResponseEntity.ok(invoiceService.getUnitCountByUserAndFeeType(usersId, feeTypeId));
    }

    // 查詢所有待審核請款單
    @GetMapping("/pending-validate")
    public ResponseEntity<List<Invoice>> getPendingValidate() {
        return ResponseEntity.ok(invoiceService.findByStatus(false));
    }

    // 批次審核請款單
    @PostMapping("/batch-validate")
    public ResponseEntity<Void> batchValidate(@RequestBody List<Integer> invoiceIds) {
        for (Integer id : invoiceIds) {
            Invoice invoice = invoiceService.findById(id);
            if (invoice != null && Boolean.FALSE.equals(invoice.getStatus())) {
                invoice.setStatus(true);
                invoiceService.save(invoice);
            }
        }
        return ResponseEntity.ok().build();
    }

}
