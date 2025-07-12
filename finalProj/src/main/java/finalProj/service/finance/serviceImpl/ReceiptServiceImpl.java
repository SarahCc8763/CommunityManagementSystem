package finalProj.service.finance.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import finalProj.domain.finance.Invoice;
import finalProj.domain.finance.Receipt;
import finalProj.dto.finance.InvoiceDTO;
import finalProj.dto.finance.ReceiptDTO;
import finalProj.repository.finance.InvoiceRepository;
import finalProj.repository.finance.ReceiptRepository;
import finalProj.service.finance.baseServiceInterfaces.ReceiptService;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Override
    public ReceiptDTO createReceipt(ReceiptDTO dto) {
        if (dto.getInvoiceId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "必須指定 invoiceId");
        }
        if (dto.getAmountPay() == null || dto.getAmountPay().signum() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "金額不得為空或負數");
        }
        Invoice invoice = invoiceRepository.findById(dto.getInvoiceId())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "發票不存在: ID = " + dto.getInvoiceId()));

        // 檢查是否已存在收據
        Optional<Receipt> existed = receiptRepository.findAll().stream()
                .filter(r -> r.getInvoice() != null && r.getInvoice().getInvoiceId().equals(dto.getInvoiceId()))
                .findFirst();
        if (existed.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "該發票已經有收據，請勿重複繳費");
        }

        Receipt receipt = new Receipt();
        receipt.setInvoice(invoice);
        receipt.setReceiptNum(generateReceiptNum());
        receipt.setPaymentMethod(dto.getPaymentMethod());
        receipt.setPaidAt(dto.getPaidAt());
        receipt.setDebitAt(dto.getDebitAt());
        receipt.setAmountPay(dto.getAmountPay());
        receipt.setInstallments(dto.getInstallments());
        receipt.setNote(dto.getNote());

        Receipt saved = receiptRepository.save(receipt);
        return toDTO(saved);
    }

    // Receipt 轉 DTO
    private ReceiptDTO toDTO(Receipt entity) {
        if (entity == null)
            return null;
        ReceiptDTO dto = new ReceiptDTO();
        dto.setReceiptId(entity.getReceiptId());
        dto.setInvoiceId(entity.getInvoice() != null ? entity.getInvoice().getInvoiceId() : null);
        dto.setPaymentMethod(entity.getPaymentMethod());
        dto.setPaidAt(entity.getPaidAt());
        dto.setDebitAt(entity.getDebitAt());
        dto.setAmountPay(entity.getAmountPay());
        dto.setInstallments(entity.getInstallments());
        dto.setNote(entity.getNote());
        // 補齊 invoice 欄位
        if (entity.getInvoice() != null) {
            dto.setInvoice(new InvoiceDTO(entity.getInvoice()));
        }
        return dto;
    }

    @Override
    public List<Receipt> findAll() {
        return receiptRepository.findAll();
    }

    @Override
    public Receipt findById(Integer id) {
        return receiptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("收據不存在: ID = " + id));
    }

    @Override
    public Receipt save(Receipt receipt) {
        return receiptRepository.save(receipt);
    }

    @Override
    public void deleteById(Integer id) {
        receiptRepository.deleteById(id);
    }

    @Override
    public List<Receipt> findByCommunityId(Integer communityId) {
        return receiptRepository.findByCommunityId(communityId);
    }

    private String generateReceiptNum() {
        // 使用 UUID 字串作為收據號碼
        return UUID.randomUUID().toString();
    }
}