package finalProj.service.finance.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

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
    public InvoiceResponse createResponse(Integer userId, InvoiceResponseDTO dto) {
        Invoice invoice = invoiceRepository.findById(dto.getInvoiceId())
                .orElseThrow(() -> new IllegalArgumentException("查無該發票 ID"));

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("查無該用戶 ID"));

        InvoiceResponse response = new InvoiceResponse();
        response.setInvoice(invoice);
        response.setUser(user);
        response.setAccountCode(dto.getAccountCode());
        response.setLastResponse(dto.getLastResponse());
        response.setLastResponseTime(LocalDateTime.now());

        return invoiceResponseRepository.save(response);
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

}
