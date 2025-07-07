package finalProj.service.finance.serviceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.finance.Invoice;
import finalProj.domain.users.UnitsUsers;
import finalProj.dto.finance.FeeTypeDTO;
import finalProj.dto.finance.InvoiceDTO;
import finalProj.dto.finance.InvoiceResponseDTO;
import finalProj.dto.finance.ReceiptDTO;
import finalProj.dto.finance.UserSimpleDTO;
import finalProj.repository.finance.InvoiceRepository;
import finalProj.repository.users.UnitsUsersRepository;
import finalProj.service.finance.baseServiceInterfaces.InvoiceService;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private UnitsUsersRepository unitsUsersRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    public Invoice save(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice findById(Integer invoiceId) {
        return invoiceRepository.findById(invoiceId).orElse(null);
    }

    @Override
    public List<Invoice> findByStatus(Boolean status) {
        return invoiceRepository.findByStatus(status);
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
    public BigDecimal getUnitCountByUserAndFeeType(Integer userId, Integer feeTypeId) {
        // 你可能需要透過 User → UnitsUsers → Units 來取得這個人的戶別資訊
        // 如果以「坪數」為例，通常只有一戶，或取最早入住那一戶的坪數

        // 以下是假設你有 UnitsUsersRepository 可以查
        UnitsUsers uu = unitsUsersRepository.findTopByUsers_UsersIdOrderByCreatedAtAsc(userId);
        if (uu != null && uu.getUnits() != null) {
            return uu.getUnits().getPing(); // ping 是代表坪數的欄位
        } else {
            return BigDecimal.ZERO; // 或拋出錯誤
        }
    }

    @Override
    public List<InvoiceDTO> findUnpaidInvoicesByUserId(Integer userId) {
        return invoiceRepository.findByUsers_UsersIdAndPaymentStatusIgnoreCase(userId, "unpaid")
                .stream()
                .map(this::toInvoiceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDTO> findUnpaidInvoicesByCommunityId(Integer communityId) {
        return invoiceRepository.findByCommunityIdAndPaymentStatusIgnoreCase(communityId, "unpaid")
                .stream()
                .map(this::toInvoiceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDTO> findUnpaidInvoicesWithResponse() {
        return invoiceRepository.findByPaymentStatusAndInvoiceResponsesIsNotEmptyIgnoreCase("unpaid")
                .stream()
                .map(this::toInvoiceDTO)
                .collect(Collectors.toList());
    }

    private InvoiceDTO toInvoiceDTO(Invoice invoice) {
        InvoiceDTO dto = new InvoiceDTO();
        dto.setInvoiceId(invoice.getInvoiceId());
        dto.setAmountDue(invoice.getAmountDue());
        dto.setPeriodName(invoice.getPeriodName());
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

        // 巢狀：User（簡易）
        if (invoice.getUsers() != null) {
            UserSimpleDTO userDTO = new UserSimpleDTO();
            userDTO.setUsersId(invoice.getUsers().getUsersId());
            userDTO.setName(invoice.getUsers().getName());
            dto.setUser(userDTO);
        }

        // 巢狀：FeeType
        if (invoice.getFeeType() != null) {
            FeeTypeDTO feeTypeDTO = new FeeTypeDTO();
            feeTypeDTO.setFeeTypeId(invoice.getFeeType().getFeeTypeId());
            feeTypeDTO.setDescription(invoice.getFeeType().getDescription());
            feeTypeDTO.setAmountPerUnit(invoice.getFeeType().getAmountPerUnit()); // ←這裡已對應命名
            feeTypeDTO.setFrequency(invoice.getFeeType().getFrequency());
            feeTypeDTO.setUnit(invoice.getFeeType().getUnit());
            feeTypeDTO.setFeeCode(invoice.getFeeType().getFeeCode());
            feeTypeDTO.setNote(invoice.getFeeType().getNote());
            feeTypeDTO.setCommunityId(invoice.getFeeType().getCommunityId());
            feeTypeDTO.setStatus(invoice.getFeeType().getStatus());
            feeTypeDTO.setCreatedBy(invoice.getFeeType().getCreatedBy());
            feeTypeDTO.setUpdatedBy(invoice.getFeeType().getUpdatedBy());
            dto.setFeeType(feeTypeDTO);
        }

        // 巢狀：InvoiceResponses
        if (invoice.getInvoiceResponses() != null && !invoice.getInvoiceResponses().isEmpty()) {
            List<InvoiceResponseDTO> responses = invoice.getInvoiceResponses().stream().map(res -> {
                InvoiceResponseDTO resDto = new InvoiceResponseDTO();
                resDto.setInvoiceResponseId(res.getInvoiceResponseId());
                resDto.setInvoiceId(invoice.getInvoiceId());
                if (res.getUser() != null) {
                    resDto.setUserId(res.getUser().getUsersId());
                }
                resDto.setAccountCode(res.getAccountCode());
                resDto.setLastResponse(res.getLastResponse());
                resDto.setLastResponseTime(res.getLastResponseTime());
                resDto.setVerified(res.getVerified());
                resDto.setVerifiedTime(res.getVerifiedTime());
                resDto.setVerifiedBy(res.getVerifiedBy());
                return resDto;
            }).collect(Collectors.toList());
            dto.setInvoiceResponses(responses);
        }

        return dto;
    }

}
