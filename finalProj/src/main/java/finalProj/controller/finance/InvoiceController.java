package finalProj.controller.finance;

import finalProj.domain.finance.Invoice;
import finalProj.service.finance.baseServiceInterfaces.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/finance/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    // 【功能】取得所有發票
    @GetMapping
    public List<Invoice> getAll() {
        return invoiceService.findAll();
    }

    // 【功能】依ID查詢單一發票
    @GetMapping("/{invoiceId}")
    public Invoice getById(@PathVariable Integer invoiceId) {
        return invoiceService.findById(invoiceId);
    }

    // 【功能】建立新發票
    @PostMapping
    public Invoice create(@RequestBody Invoice invoice) {
        return invoiceService.save(invoice);
    }

    // 【功能】更新發票內容
    @PutMapping("/{invoiceId}")
    public Invoice update(@PathVariable Integer invoiceId, @RequestBody Invoice updated) {
        updated.setInvoiceId(invoiceId);
        return invoiceService.save(updated);
    }

    // 【功能】刪除發票
    @DeleteMapping("/{invoiceId}")
    public void delete(@PathVariable Integer invoiceId) {
        invoiceService.deleteById(invoiceId);
    }

    // 【功能】依期別代碼產生/查詢該期所有發票
    @GetMapping("/generate/{periodCode}")
    public List<Invoice> generateByPeriod(@PathVariable String periodCode) {
        return invoiceService.generateInvoicesForPeriod(periodCode);
    }

    // 【功能】更新發票狀態（如已繳/未繳）
    @PutMapping("/status/{invoiceId}")
    public Invoice updateStatus(@PathVariable Integer invoiceId, @RequestParam Boolean status) {
        return invoiceService.updateInvoiceStatus(invoiceId, status);
    }
}
