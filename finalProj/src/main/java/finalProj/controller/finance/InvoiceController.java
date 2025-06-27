
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

    @GetMapping
    public List<Invoice> getAll() {
        return invoiceService.findAll();
    }

    @GetMapping("/{invoiceId}")
    public Invoice getById(@PathVariable Integer invoiceId) {
        return invoiceService.findById(invoiceId);
    }

    @PostMapping
    public Invoice create(@RequestBody Invoice invoice) {
        return invoiceService.save(invoice);
    }

    @PutMapping("/{invoiceId}")
    public Invoice update(@PathVariable Integer invoiceId, @RequestBody Invoice updated) {
        updated.setInvoiceId(invoiceId);
        return invoiceService.save(updated);
    }

    @DeleteMapping("/{invoiceId}")
    public void delete(@PathVariable Integer invoiceId) {
        invoiceService.deleteById(invoiceId);
    }

    @GetMapping("/generate/{periodCode}")
    public List<Invoice> generateByPeriod(@PathVariable String periodCode) {
        return invoiceService.generateInvoicesForPeriod(periodCode);
    }

    @PutMapping("/status/{invoiceId}")
    public Invoice updateStatus(@PathVariable Integer invoiceId, @RequestParam Boolean status) {
        return invoiceService.updateInvoiceStatus(invoiceId, status);
    }
}
