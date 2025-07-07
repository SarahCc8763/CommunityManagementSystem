package finalProj.controller.finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProj.dto.finance.FeeGenerationRequest;
import finalProj.service.finance.baseServiceInterfaces.InvoiceGeneratingService;

@RestController
@RequestMapping("/api/finance/invoice-generator")
public class InvoiceGeneratingController {

    @Autowired
    private InvoiceGeneratingService invoiceGeneratingService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateInvoices(@RequestBody FeeGenerationRequest request) {
        try {
            invoiceGeneratingService.generateInvoices(
                    request.getFeeTypeId(),
                    request.getBillingPeriodId());
            return ResponseEntity.ok("已成功產生帳單");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("產生失敗：" + e.getMessage());
        }
    }
}
