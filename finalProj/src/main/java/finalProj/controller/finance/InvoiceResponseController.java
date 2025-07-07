package finalProj.controller.finance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import finalProj.dto.finance.InvoiceDTO;
import finalProj.dto.finance.InvoiceResponseDTO;
import finalProj.service.finance.baseServiceInterfaces.InvoiceResponseService;
import finalProj.service.finance.baseServiceInterfaces.InvoiceService;

@RestController
@RequestMapping("/finance/invoice-responses")
public class InvoiceResponseController {

    @Autowired
    private InvoiceResponseService invoiceResponseService;

    @Autowired
    private InvoiceService invoiceService;

    // 【功能】使用者回覆匯款（填寫帳號末五碼與備註）
    @PostMapping
    public ResponseEntity<InvoiceResponseDTO> respondInvoice(
            @RequestParam Integer userId,
            @RequestBody InvoiceResponseDTO dto) {
        InvoiceResponseDTO saved = invoiceResponseService.createResponse(userId, dto);
        return ResponseEntity.ok(saved);
    }

    // 【功能】使用者查詢自己所有回覆紀錄
    @GetMapping("/my")
    public ResponseEntity<List<InvoiceResponseDTO>> getMyResponses(@RequestParam Integer userId) {
        List<InvoiceResponseDTO> list = invoiceResponseService.findByUserId(userId);
        return ResponseEntity.ok(list);
    }

    // 【功能】管理員查詢所有用戶的回覆紀錄
    @GetMapping("/all")
    public ResponseEntity<List<InvoiceResponseDTO>> getAllResponses() {
        List<InvoiceResponseDTO> list = invoiceResponseService.findAllDTO();
        return ResponseEntity.ok(list);
    }

    // 【功能】管理員查詢單一回覆明細
    @GetMapping("/adminSearch/{id}")
    public ResponseEntity<InvoiceResponseDTO> getResponseById(@PathVariable Integer id) {
        InvoiceResponseDTO dto = invoiceResponseService.findDTOById(id);
        return ResponseEntity.ok(dto);
    }

    // 【功能】管理員審核用戶回覆（確認匯款）
    @PutMapping("/adminRespond/{id}/verify")
    public ResponseEntity<InvoiceResponseDTO> verifyResponse(
            @PathVariable Integer id,
            @RequestParam Integer adminId) {
        InvoiceResponseDTO dto = invoiceResponseService.verifyResponse(id, adminId);
        return ResponseEntity.ok(dto);
    }

    // 【功能】依 invoiceId 查詢所有回覆
    @GetMapping("/by-invoice")
    public ResponseEntity<List<InvoiceResponseDTO>> getResponsesByInvoiceId(@RequestParam Integer invoiceId) {
        List<InvoiceResponseDTO> list = invoiceResponseService.findByInvoiceId(invoiceId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/with-response/unpaid")
    public ResponseEntity<List<InvoiceDTO>> getUnpaidInvoicesWithResponses() {
        List<InvoiceDTO> list = invoiceService.findUnpaidInvoicesWithResponse();
        return ResponseEntity.ok(list);
    }
}