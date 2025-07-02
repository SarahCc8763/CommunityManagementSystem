package finalProj.service.finance.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import finalProj.domain.finance.Invoice;
import finalProj.domain.finance.InvoiceResponse;
import finalProj.domain.users.Users;
import finalProj.dto.finance.InvoiceResponseDTO;
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
        // 檢查是否本人
        if (!invoice.getUsers().getUsersId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "只能回覆自己的發票");
        }
        // 檢查是否已回覆
        boolean already = invoiceResponseRepository.findAll().stream()
                .anyMatch(r -> r.getInvoice() != null && r.getInvoice().getInvoiceId().equals(dto.getInvoiceId())
                        && r.getUser() != null && r.getUser().getUsersId().equals(userId));
        if (already) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "已回覆過此發票");
        }
        // 檢查帳號末五碼格式
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
        // 新增：同步將 invoice 狀態設為 PENDING
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
        // TODO Auto-generated method stub
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
        // 同步將 invoice 狀態設為 PAID
        Invoice invoice = entity.getInvoice();
        if (invoice != null) {
            invoice.setPaymentStatus("PAID");
            invoiceRepository.save(invoice);
        }
        InvoiceResponse saved = invoiceResponseRepository.save(entity);
        return toDTO(saved);
    }

    // Entity 轉 DTO
    private InvoiceResponseDTO toDTO(InvoiceResponse entity) {
        if (entity == null)
            return null;
        InvoiceResponseDTO dto = new InvoiceResponseDTO();
        dto.setInvoiceResponseId(entity.getInvoiceResponseId());
        dto.setInvoiceId(entity.getInvoice() != null ? entity.getInvoice().getInvoiceId() : null);
        dto.setUserId(entity.getUser() != null ? entity.getUser().getUsersId() : null);
        dto.setAccountCode(entity.getAccountCode());
        dto.setLastResponse(entity.getLastResponse());
        dto.setLastResponseTime(entity.getLastResponseTime());
        dto.setVerified(entity.getVerified());
        dto.setVerifiedTime(entity.getVerifiedTime());
        dto.setVerifiedBy(entity.getVerifiedBy());
        return dto;
    }

}
