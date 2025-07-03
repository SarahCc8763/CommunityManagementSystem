package finalProj.service.finance.baseServiceInterfaces;

import finalProj.domain.finance.InvoiceResponse;
import finalProj.dto.finance.InvoiceResponseDTO;
import java.util.List;

public interface InvoiceResponseService extends BaseService<InvoiceResponse, Integer> {
    InvoiceResponseDTO createResponse(Integer userId, InvoiceResponseDTO dto);

    List<InvoiceResponseDTO> findByUserId(Integer userId);

    List<InvoiceResponseDTO> findAllDTO();

    InvoiceResponseDTO findDTOById(Integer id);

    InvoiceResponseDTO verifyResponse(Integer id, Integer adminId);

    List<InvoiceResponseDTO> findByInvoiceId(Integer invoiceId);
}
