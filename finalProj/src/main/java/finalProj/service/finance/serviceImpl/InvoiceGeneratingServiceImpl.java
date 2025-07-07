package finalProj.service.finance.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.finance.BillingPeriod;
import finalProj.domain.finance.FeeType;
import finalProj.domain.finance.Invoice;
import finalProj.domain.users.Units;
import finalProj.domain.users.UnitsUsers;
import finalProj.domain.users.Users;
import finalProj.repository.finance.BillingPeriodRepository;
import finalProj.repository.finance.FeeTypeRepository;
import finalProj.repository.finance.InvoiceRepository;
import finalProj.repository.users.UnitsRepository;
import finalProj.repository.users.UnitsUsersRepository;
import finalProj.service.finance.baseServiceInterfaces.InvoiceGeneratingService;
import jakarta.transaction.Transactional;

@Transactional
@Service
public class InvoiceGeneratingServiceImpl implements InvoiceGeneratingService {

    @Autowired
    private UnitsRepository unitsRepository;

    @Autowired
    private UnitsUsersRepository unitsUsersRepository;

    // @Autowired
    // private ParkingRepository parkingRepository;

    @Autowired
    private FeeTypeRepository feeTypeRepository;

    @Autowired
    private BillingPeriodRepository billingPeriodRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Transactional
    public void generateInvoices(Integer feeTypeId, Integer billingPeriodId) {
        System.out.println(">>>>> 產生繳款單開始，feeTypeId: " + feeTypeId + ", billingPeriodId: " + billingPeriodId);
        FeeType feeType = feeTypeRepository.findById(feeTypeId)
                .orElseThrow(() -> new IllegalArgumentException("無效的費用類別ID: " + feeTypeId));
        BillingPeriod billingPeriod = billingPeriodRepository.findById(billingPeriodId)
                .orElseThrow(() -> new IllegalArgumentException("無效的期別ID: " + billingPeriodId));

        // 只抓該社區的戶
        List<Units> units = unitsRepository.findAll(); // 若有findByCommunityId可改用
        for (Units unit : units) {
            // 若有社區過濾
            if (feeType.getCommunityId() != null && unit.getCommunity() != null &&
                    !feeType.getCommunityId().equals(unit.getCommunity().getCommunityId())) {
                System.out.println(">>> 忽略單位：" + unit.getUnit() + " 社區不符");
                continue;

            }

            System.out.println(">>> 准備新增繳費單 for unit " + unit.getUnit());
            List<UnitsUsers> unitUsers = unitsUsersRepository.findByUnitOrderByUser_UsersIdAsc(unit);
            if (unitUsers == null || unitUsers.isEmpty())
                continue;
            Users user = unitUsers.get(0).getUser(); // 主用戶
            java.math.BigDecimal unitCount;
            if ("每坪".equals(feeType.getUnit())) {
                unitCount = unit.getPing();
            } else {
                unitCount = java.math.BigDecimal.ONE;
            }
            java.math.BigDecimal unitPrice = feeType.getAmountPerUnit();
            if (unitPrice == null || unitCount == null)
                continue;
            java.math.BigDecimal totalAmount = unitCount.multiply(unitPrice);

            Invoice invoice = new Invoice();
            invoice.setUsers(user);
            invoice.setFeeType(feeType);
            invoice.setBillingPeriod(billingPeriod);
            invoice.setPeriodName(billingPeriod.getPeriodName());
            invoice.setDeadline(billingPeriod.getDueDate());
            invoice.setUnitCount(unitCount);
            invoice.setUnitPrice(unitPrice);
            invoice.setTotalAmount(totalAmount);
            invoice.setAmountDue(totalAmount);
            invoice.setPaymentStatus("unpaid");
            invoice.setCommunityId(feeType.getCommunityId());
            invoice.setStatus(false);
            invoice.setNote("system generated");
            invoice.setCreatedAt(LocalDateTime.now());
            invoiceRepository.save(invoice);
            System.out.println(">>> 儲存 invoice 給 unit: " + unit.getUnit());
        }
    }
}
