
package finalProj.service.finance.serviceImpl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.finance.BillingPeriod;
import finalProj.domain.finance.FeeType;
import finalProj.domain.finance.Invoice;
import finalProj.domain.users.Units;
import finalProj.domain.users.UnitsUsers;
import finalProj.domain.users.Users;
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

    // @Autowired
    // private FeeTypeRepository feeTypeRepository;

    // @Autowired
    // private BillingPeriodRepository billingPeriodRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    // @Override
    // public void generateInvoices(Integer feeTypeId, String billingPeriodCode,
    // BigDecimal unitPrice) {
    // Optional<FeeType> feeTypeOpt = feeTypeRepository.findById(feeTypeId);
    // FeeType feeType = feeTypeOpt.orElseThrow(() -> new
    // IllegalArgumentException("無效的費用類別 ID：" + feeTypeId));

    // BillingPeriod billingPeriod =
    // billingPeriodRepository.findByPeriodCode(billingPeriodCode)
    // .orElseThrow(() -> new IllegalArgumentException("無效的期別代碼：" +
    // billingPeriodCode));

    // switch (feeTypeName) {
    // case "管理費":
    // generateManagementFeeInvoices(feeType, billingPeriod);
    // break;
    // case "清潔費":
    // generateCleaningFeeInvoices(feeType, billingPeriod);
    // break;
    // // case "車位管理費":
    // // generateParkingFeeInvoices(feeType, billingPeriod, unitPrice);
    // // break;
    // default:
    // throw new IllegalArgumentException("不支援的費用類別：" + feeTypeName);
    // }
    // }

    // 產生以坪數為單位的管理費

    private void generateManagementFeeInvoices(FeeType feeType, BillingPeriod billingPeriod) {
        List<Units> units = unitsRepository.findAll();

        for (Units unit : units) {
            List<UnitsUsers> unitUsers = unitsUsersRepository.findByUnitOrderByUser_UsersIdAsc(unit);
            if (unitUsers == null || unitUsers.isEmpty())
                continue;
            BigDecimal unitCount = unit.getPing();
            Users user = unitUsers.get(0).getUser(); // 使用最小的 usersId
            BigDecimal unitPrice = feeType.getAmountPerUnit();
            if (unitPrice == null || unitCount == null) {
                throw new IllegalArgumentException("單價或坪數為 null，無法計算金額");
            }
            BigDecimal totalAmount = unitCount.multiply(unitPrice);

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

            invoiceRepository.save(invoice);
        }
    }

    // 清潔費
    private void generateCleaningFeeInvoices(FeeType feeType, BillingPeriod billingPeriod) {
        List<Units> units = unitsRepository.findAll();

        for (Units unit : units) {
            List<UnitsUsers> unitUsers = unitsUsersRepository.findByUnitOrderByUser_UsersIdAsc(unit);
            if (unitUsers == null || unitUsers.isEmpty())
                continue;

            Users user = unitUsers.get(0).getUser(); // 使用最小的 usersId
            BigDecimal unitCount = BigDecimal.ONE;
            BigDecimal unitPrice = feeType.getAmountPerUnit();
            BigDecimal totalAmount = unitPrice;

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

            invoiceRepository.save(invoice);
        }
    }
    // // 產生車位Invoice

    // private void generateParkingFeeInvoices(FeeType feeType, BillingPeriod
    // billingPeriod, BigDecimal unitPrice) {
    // List<Parking> parkings = parkingRepository.findAll();

    // for (Parking parking : parkings) {
    // Users owner = parking.getOwner();
    // if (owner != null) {
    // Invoice invoice = new Invoice();
    // invoice.setUser(owner);
    // invoice.setFeeType(feeType);
    // invoice.setBillingPeriod(billingPeriod);
    // invoice.setUnitCount(BigDecimal.ONE);
    // invoice.setUnitPrice(unitPrice);
    // invoice.setAmount(unitPrice);

    // invoiceRepository.save(invoice);
    // }
    // }
    // }
}
