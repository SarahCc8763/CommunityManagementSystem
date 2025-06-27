package finalProj.service.finance.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.finance.Invoice;
import finalProj.domain.finance.Receipt;
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
    public Receipt createReceipt(ReceiptDTO dto) {
        Invoice invoice = invoiceRepository.findById(dto.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("發票不存在: ID = " + dto.getInvoiceId()));

        Receipt receipt = new Receipt();
        receipt.setInvoice(invoice);
        receipt.setReceiptNum(generateReceiptNum());
        receipt.setPaymentMethod(dto.getPaymentMethod());
        receipt.setPaidAt(dto.getPaidAt());
        receipt.setDebitAt(dto.getDebitAt());
        receipt.setAmountPay(dto.getAmountPay());
        receipt.setInstallments(dto.getInstallments());

        return receiptRepository.save(receipt);
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

    private Integer generateReceiptNum() {
        // 可替換為實際邏輯，例如資料庫流水號或時間戳
        return (int) (System.currentTimeMillis() % 1000000);
    }
}