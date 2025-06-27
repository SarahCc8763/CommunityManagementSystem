package finalProj.controller.finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.finance.InvoiceResponse;
import finalProj.dto.finance.InvoiceResponseDTO;
import finalProj.service.finance.baseServiceInterfaces.InvoiceResponseService;

@RestController
@RequestMapping("/finance/invoice-responses")
public class InvoiceResponseController {

    @Autowired
    private InvoiceResponseService invoiceResponseService;

    // ⚠️ 暫時手動帶 userId，未來可從登入資訊取得
    @PostMapping
    public ResponseEntity<InvoiceResponse> respondInvoice(
            @RequestParam Integer userId,
            @RequestBody InvoiceResponseDTO dto) {
        InvoiceResponse saved = invoiceResponseService.createResponse(userId, dto);
        return ResponseEntity.ok(saved);
    }
}