package finalProj.service.finance.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.finance.Invoice;
import finalProj.domain.finance.FeeType;
import finalProj.domain.users.UnitsUsers;
import finalProj.repository.finance.InvoiceRepository;
import finalProj.repository.finance.FeeTypeRepository;
import finalProj.repository.users.UnitsUsersRepository;
import finalProj.service.finance.baseServiceInterfaces.InvoiceService;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private FeeTypeRepository feeTypeRepository;

    @Autowired
    private UnitsUsersRepository unitsUsersRepository;

    @Override
    public Invoice save(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice findById(Integer invoiceId) {
        return invoiceRepository.findById(invoiceId).orElse(null);
    }

    @Override
    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    @Override
    public void deleteById(Integer invoiceId) {
        invoiceRepository.deleteById(invoiceId);
    }

    @Override
    public List<Invoice> generateInvoicesForPeriod(String periodCode) {
        // 實際邏輯會根據需求從 repository 中查詢或產生發票
        return invoiceRepository.findByBillingPeriod_PeriodCode(periodCode);
    }

    @Override
    public Invoice updateInvoiceStatus(Integer invoiceId, Boolean status) {
        Optional<Invoice> optional = invoiceRepository.findById(invoiceId);
        if (optional.isPresent()) {
            Invoice invoice = optional.get();
            invoice.setStatus(status);
            return invoiceRepository.save(invoice);
        }
        return null;
    }

    @Override
    public java.math.BigDecimal getUnitCountByUserAndFeeType(Integer usersId, Integer feeTypeId) {
        FeeType feeType = feeTypeRepository.findById(feeTypeId).orElse(null);
        if (feeType == null)
            return java.math.BigDecimal.ONE;
        if ("管理費".equals(feeType.getDescription())) {
            // 取得用戶對應的單位坪數
            java.util.List<UnitsUsers> list = unitsUsersRepository.findByUser_UsersId(usersId);
            if (list != null && !list.isEmpty() && list.get(0).getUnit() != null) {
                return list.get(0).getUnit().getPing();
            }
        }
        return java.math.BigDecimal.ONE;
    }

}
