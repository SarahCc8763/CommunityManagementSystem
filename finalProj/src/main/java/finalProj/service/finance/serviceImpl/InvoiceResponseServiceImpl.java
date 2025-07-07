package finalProj.service.finance.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import finalProj.domain.finance.BillingPeriod;
import finalProj.domain.finance.FeeType;
import finalProj.domain.finance.Invoice;
import finalProj.domain.finance.InvoiceResponse;
import finalProj.domain.users.Users;
import finalProj.dto.finance.BillingPeriodDTO;
import finalProj.dto.finance.FeeTypeDTO;
import finalProj.dto.finance.InvoiceDTO;
import finalProj.dto.finance.InvoiceResponseDTO;
import finalProj.dto.finance.UserSimpleDTO;
import finalProj.repository.finance.InvoiceRepository;
import finalProj.repository.finance.InvoiceResponseRepository;
import finalProj.repository.users.UsersRepository;
import finalProj.service.finance.baseServiceInterfaces.InvoiceResponseService;

@Service
public class InvoiceResponseServiceImpl implements InvoiceResponseService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceResponseRepository invoiceResponseRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public InvoiceResponseDTO createResponse(Integer userId, InvoiceResponseDTO dto) {
        Invoice invoice = invoiceRepository.findById(dto.getInvoiceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "查無該發票 ID"));
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "查無該用戶 ID"));
        if (!invoice.getUsers().getUsersId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "只能回覆自己的發票");
        }
        if (dto.getAccountCode() == null || !dto.getAccountCode().matches("\\d{5}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "帳號末五碼格式錯誤");
        }
        InvoiceResponse response = new InvoiceResponse();
        response.setInvoice(invoice);
        response.setUser(user);
        response.setAccountCode(dto.getAccountCode());
        response.setLastResponse(dto.getLastResponse());
        response.setLastResponseTime(LocalDateTime.now());
        response.setVerified(false);
        invoice.setPaymentStatus("PENDING");
        invoiceRepository.save(invoice);
        InvoiceResponse saved = invoiceResponseRepository.save(response);
        return toDTO(saved);
    }

    @Override
    public void deleteById(Integer id) {
        invoiceResponseRepository.deleteById(id);
    }

    @Override
    public InvoiceResponse findById(Integer id) {
        return invoiceResponseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到回應資料"));
    }

    @Override
    public List<InvoiceResponse> findAll() {
        return invoiceResponseRepository.findAll();
    }

    @Override
    public InvoiceResponse save(InvoiceResponse entity) {
        return null;
    }

    @Override
    public List<InvoiceResponseDTO> findByUserId(Integer userId) {
        return invoiceResponseRepository.findAll().stream()
                .filter(r -> r.getUser() != null && r.getUser().getUsersId().equals(userId))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceResponseDTO> findAllDTO() {
        return invoiceResponseRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public InvoiceResponseDTO findDTOById(Integer id) {
        InvoiceResponse entity = invoiceResponseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到回應資料"));
        return toDTO(entity);
    }

    @Override
    public InvoiceResponseDTO verifyResponse(Integer id, Integer adminId) {
        InvoiceResponse entity = invoiceResponseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到回應資料"));
        if (entity.getVerified() != null && entity.getVerified()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "已審核過");
        }
        entity.setVerified(true);
        entity.setVerifiedTime(LocalDateTime.now());
        entity.setVerifiedBy(adminId);
        Invoice invoice = entity.getInvoice();
        if (invoice != null) {
            invoice.setPaymentStatus("paid");
            invoiceRepository.save(invoice);
        }
        InvoiceResponse saved = invoiceResponseRepository.save(entity);
        return toDTO(saved);
    }

    @Override
    public List<InvoiceResponseDTO> findByInvoiceId(Integer invoiceId) {
        List<InvoiceResponse> list = invoiceResponseRepository.findByInvoice_InvoiceId(invoiceId);
        return list.stream().map(this::toDTO).toList();
    }

    @Override
    public List<InvoiceDTO> findUnpaidInvoicesWithResponse() {
        List<Invoice> invoices = invoiceRepository
                .findByPaymentStatusAndInvoiceResponsesIsNotEmpty("unpaid");
        return invoices.stream().map(this::toInvoiceDTO).collect(Collectors.toList());
    }

    private BillingPeriodDTO toBillingPeriodDTO(BillingPeriod entity) {
        if (entity == null)
            return null;
        BillingPeriodDTO dto = new BillingPeriodDTO();
        dto.setBillingPeriodId(entity.getBillingPeriodId());
        dto.setPeriodCode(entity.getPeriodCode());
        dto.setPeriodName(entity.getPeriodName());
        return dto;
    }

    private InvoiceResponseDTO toDTO(InvoiceResponse entity) {
        if (entity == null)
            return null;
        InvoiceResponseDTO dto = new InvoiceResponseDTO();
        dto.setInvoiceResponseId(entity.getInvoiceResponseId());
        dto.setInvoiceId(entity.getInvoice() != null ? entity.getInvoice().getInvoiceId() : null);
        dto.setAccountCode(entity.getAccountCode());
        dto.setLastResponse(entity.getLastResponse());
        dto.setLastResponseTime(entity.getLastResponseTime());
        dto.setVerified(entity.getVerified());
        dto.setVerifiedTime(entity.getVerifiedTime());
        dto.setVerifiedBy(entity.getVerifiedBy());
        return dto;
    }

    private FeeTypeDTO toFeeTypeDTO(FeeType feeType) {
        if (feeType == null)
            return null;
        FeeTypeDTO dto = new FeeTypeDTO();
        dto.setFeeTypeId(feeType.getFeeTypeId());
        dto.setDescription(feeType.getDescription());
        return dto;
    }

    private InvoiceDTO toInvoiceDTO(Invoice invoice) {
        if (invoice == null)
            return null;

        InvoiceDTO dto = new InvoiceDTO();
        dto.setInvoiceId(invoice.getInvoiceId());
        dto.setAmountDue(invoice.getAmountDue());
        dto.setPaymentStatus(invoice.getPaymentStatus());
        dto.setDeadline(invoice.getDeadline());
        dto.setNote(invoice.getNote());

        if (invoice.getUsers() != null) {
            UserSimpleDTO userDto = new UserSimpleDTO();
            userDto.setUsersId(invoice.getUsers().getUsersId());
            userDto.setName(invoice.getUsers().getName());
            dto.setUser(userDto);
        }

        dto.setFeeType(toFeeTypeDTO(invoice.getFeeType()));

        dto.setBillingPeriod(toBillingPeriodDTO(invoice.getBillingPeriod()));

        if (invoice.getInvoiceResponses() != null && !invoice.getInvoiceResponses().isEmpty()) {
            List<InvoiceResponseDTO> responseDTOs = invoice.getInvoiceResponses().stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
            dto.setInvoiceResponses(responseDTOs);
        }

        return dto;
    }

}
