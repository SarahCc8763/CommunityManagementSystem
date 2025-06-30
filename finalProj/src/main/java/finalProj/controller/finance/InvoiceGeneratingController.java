package finalProj.controller.finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProj.dto.finance.InvoiceGenerateRequest;
import finalProj.service.finance.baseServiceInterfaces.InvoiceGeneratingService;

@RestController
@RequestMapping("/finance/invoice-generator")
public class InvoiceGeneratingController {

    @Autowired
    private InvoiceGeneratingService invoiceGeneratingService;

    // 【功能】批次產生請款（依費用類型、期別、單價）
    @PostMapping("/generate")
    public ResponseEntity<String> generateInvoices(@RequestBody InvoiceGenerateRequest request) {
        invoiceGeneratingService.generateInvoices(request.getFeeTypeName(), request.getBillingPeriodCode(),
                request.getUnitPrice());
        return ResponseEntity.ok("Invoice generation complete");
    }
}
