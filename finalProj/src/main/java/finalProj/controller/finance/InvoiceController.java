package finalProj.controller.finance;

import java.util.List;
import java.util.Map;

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

import finalProj.domain.finance.Invoice;
import finalProj.dto.finance.InvoiceDTO;
import finalProj.service.finance.baseServiceInterfaces.InvoiceService;

@RestController
@RequestMapping("/finance/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    // 查詢登入者未繳帳單（傳 userId）
    @PostMapping("/unpaid/user")
    public ResponseEntity<List<InvoiceDTO>> getUnpaidInvoicesByUser(@RequestBody Map<String, Integer> payload) {
        Integer userId = payload.get("userId");
        return ResponseEntity.ok(invoiceService.findUnpaidInvoicesByUserId(userId));
    }

    // 【功能】取得所有發票（可依communityId查詢）// 查詢社區內所有未繳帳單（傳 communityId）
    @PostMapping("/unpaid/by-community")
    public ResponseEntity<List<InvoiceDTO>> getUnpaidInvoicesByCommunity(@RequestBody Map<String, Integer> payload) {
        Integer communityId = payload.get("communityId");
        return ResponseEntity.ok(invoiceService.findUnpaidInvoicesByCommunityId(communityId));
    }

    // 【功能】依ID查詢單一發票
    @GetMapping("/getOne/{invoiceId}")
    public Invoice getById(@PathVariable Integer invoiceId) {
        return invoiceService.findById(invoiceId);
    }

    // 【功能】建立新請款
    @PostMapping
    public Invoice create(@RequestBody Invoice invoice) {
        return invoiceService.save(invoice);
    }

    // 【功能】更新請款內容
    @PutMapping("/updateOne/{invoiceId}")
    public Invoice update(@PathVariable Integer invoiceId, @RequestBody Invoice updated) {
        updated.setInvoiceId(invoiceId);
        return invoiceService.save(updated);
    }

    // 【功能】刪除請款
    @DeleteMapping("/{invoiceId}")
    public void delete(@PathVariable Integer invoiceId) {
        invoiceService.deleteById(invoiceId);
    }

    // 【功能】依期別代碼產生/查詢該期所有請款
    @GetMapping("/generate/{periodCode}")
    public List<Invoice> generateByPeriod(@PathVariable String periodCode) {
        return invoiceService.generateInvoicesForPeriod(periodCode);
    }

    // 【功能】更新請款狀態（如已繳/未繳/審核中）
    @PutMapping("/status/{invoiceId}")
    public Invoice updateStatus(@PathVariable Integer invoiceId, @RequestParam String status) {
        Invoice invoice = invoiceService.findById(invoiceId);
        invoice.setPaymentStatus(status);
        return invoiceService.save(invoice);
    }

    // 新增API：取得單位數
    @GetMapping("/unit-count")
    public java.math.BigDecimal getUnitCount(@RequestParam Integer usersId, @RequestParam Integer feeTypeId) {
        return invoiceService.getUnitCountByUserAndFeeType(usersId, feeTypeId);
    }

    // 查詢所有待審核請款單
    @GetMapping("/pending-validate")
    public List<Invoice> getPendingValidate() {
        return invoiceService.findByStatus(false);
    }

    // 批次審核API
    @PostMapping("/batch-validate")
    public void batchValidate(@RequestBody List<Integer> invoiceIds) {
        for (Integer id : invoiceIds) {
            Invoice invoice = invoiceService.findById(id);
            if (invoice != null && Boolean.FALSE.equals(invoice.getStatus())) {
                invoice.setStatus(true);
                invoiceService.save(invoice);
            }
        }
    }

}
