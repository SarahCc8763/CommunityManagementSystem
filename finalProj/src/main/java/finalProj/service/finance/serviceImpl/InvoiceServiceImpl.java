package finalProj.service.finance.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.finance.FeeType;
import finalProj.domain.finance.Invoice;
import finalProj.domain.users.UnitsUsers;
import finalProj.dto.finance.BillingPeriodDTO;
import finalProj.dto.finance.FeeTypeDTO;
import finalProj.dto.finance.InvoiceDTO;
import finalProj.dto.finance.UserSimpleDTO;
import finalProj.repository.finance.FeeTypeRepository;
import finalProj.repository.finance.InvoiceRepository;
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
    public List<Invoice> findUnpaidInvoices() {
        return invoiceRepository.findByPaymentStatusNotIgnoreCase("paid");
    }

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
    public Invoice updateInvoiceStatus(Integer invoiceId, String status) {
        Optional<Invoice> optional = invoiceRepository.findById(invoiceId);
        if (optional.isPresent()) {
            Invoice invoice = optional.get();
            invoice.setPaymentStatus(status);
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

    @Override
    public List<Invoice> findByCommunityId(Integer communityId) {
        return invoiceRepository.findByCommunityId(communityId);
    }

    @Override
    public List<InvoiceDTO> findUnpaidInvoicesByCommunityId(Integer communityId) {
        List<Invoice> list = invoiceRepository.findByCommunityIdAndPaymentStatus(communityId, "unpaid");
        return list.stream()
                .map(InvoiceDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<Invoice> findByStatus(Boolean status) {
        return invoiceRepository.findByStatus(status);
    }

    @Override
    public List<InvoiceDTO> findUnpaidInvoicesByUserId(Integer usersId) {
        return invoiceRepository.findByUsers_UsersIdAndPaymentStatusIgnoreCase(usersId, "unpaid")
                .stream()
                .map(InvoiceDTO::new) // ✅ 統一走 DTO 建構子
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDTO> findUnpaidOrPendingInvoicesByUserId(Integer userId) {
        return invoiceRepository.findUnpaidOrPendingByUserId(userId)
                .stream().map(InvoiceDTO::new).collect(java.util.stream.Collectors.toList());
    }

    private InvoiceDTO toDTO(Invoice invoice) {
        if (invoice == null)
            return null;

        InvoiceDTO dto = new InvoiceDTO();
        dto.setInvoiceId(invoice.getInvoiceId());
        dto.setAmountDue(invoice.getAmountDue());

        dto.setDeadline(invoice.getDeadline());
        dto.setPaymentPlan(invoice.getPaymentPlan());
        dto.setUnitCount(invoice.getUnitCount());
        dto.setUnitPrice(invoice.getUnitPrice());
        dto.setTotalAmount(invoice.getTotalAmount());
        dto.setPaymentStatus(invoice.getPaymentStatus());
        dto.setNote(invoice.getNote());
        dto.setCommunityId(invoice.getCommunityId());
        dto.setCreatedBy(invoice.getCreatedBy());
        dto.setUpdatedBy(invoice.getUpdatedBy());
        dto.setPeriodName(invoice.getBillingPeriod().getPeriodName());

        // 巢狀物件處理（視情況而定，以下為空值判斷）
        if (invoice.getUsers() != null) {
            UserSimpleDTO userDTO = new UserSimpleDTO();
            userDTO.setUsersId(invoice.getUsers().getUsersId());
            userDTO.setName(invoice.getUsers().getName());
            dto.setUser(userDTO);
        }

        if (invoice.getFeeType() != null) {
            FeeTypeDTO feeTypeDTO = new FeeTypeDTO();
            feeTypeDTO.setFeeTypeId(invoice.getFeeType().getFeeTypeId());
            feeTypeDTO.setDescription(invoice.getFeeType().getDescription());
            feeTypeDTO.setAmountPerUnit(invoice.getFeeType().getAmountPerUnit());
            feeTypeDTO.setFrequency(invoice.getFeeType().getFrequency());
            feeTypeDTO.setUnit(invoice.getFeeType().getUnit());
            feeTypeDTO.setStatus(invoice.getFeeType().getStatus());
            dto.setFeeType(feeTypeDTO);
        }

        if (invoice.getBillingPeriod() != null) {
            BillingPeriodDTO bpDTO = new BillingPeriodDTO();
            bpDTO.setBillingPeriodId(invoice.getBillingPeriod().getBillingPeriodId());
            bpDTO.setPeriodCode(invoice.getBillingPeriod().getPeriodCode());
            bpDTO.setPeriodName(invoice.getBillingPeriod().getPeriodName());
            bpDTO.setDueDate(invoice.getBillingPeriod().getDueDate());
            dto.setBillingPeriod(bpDTO);
        }

        return dto;
    }

}
