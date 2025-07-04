package finalProj.controller.finance;

import finalProj.domain.finance.Invoice;
import finalProj.service.finance.baseServiceInterfaces.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/finance/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/unpaid")
    public List<Invoice> getUnpaidInvoices(@RequestParam Integer userId) {
        return invoiceService.findUnpaidInvoicesByUserId(userId);
    }

    // 【功能】取得所有發票（可依communityId查詢）
    @GetMapping
    public List<Invoice> getAll(@RequestParam(required = false) Integer communityId) {
        if (communityId != null) {
            return invoiceService.findByCommunityId(communityId);
        } else {
            return invoiceService.findAll();
        }
    }

    // 【功能】依ID查詢單一發票
    @GetMapping("/{invoiceId}")
    public Invoice getById(@PathVariable Integer invoiceId) {
        return invoiceService.findById(invoiceId);
    }

    // 【功能】建立新請款
    @PostMapping
    public Invoice create(@RequestBody Invoice invoice) {
        return invoiceService.save(invoice);
    }

    // 【功能】更新請款內容
    @PutMapping("/{invoiceId}")
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
