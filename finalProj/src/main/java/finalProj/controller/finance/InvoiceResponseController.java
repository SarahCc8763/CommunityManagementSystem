package finalProj.controller.finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import finalProj.dto.finance.InvoiceResponseDTO;
import finalProj.service.finance.baseServiceInterfaces.InvoiceResponseService;

import java.util.List;

@RestController
@RequestMapping("/finance/invoice-responses")
public class InvoiceResponseController {

    @Autowired
    private InvoiceResponseService invoiceResponseService;

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
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponseDTO> getResponseById(@PathVariable Integer id) {
        InvoiceResponseDTO dto = invoiceResponseService.findDTOById(id);
        return ResponseEntity.ok(dto);
    }

    // 【功能】管理員審核用戶回覆（確認匯款）
    @PutMapping("/{id}/verify")
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
}