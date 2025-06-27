package finalProj.service.finance.baseServiceInterfaces;

import finalProj.domain.finance.InvoiceResponse;
import finalProj.dto.finance.InvoiceResponseDTO;

public interface InvoiceResponseService extends BaseService<InvoiceResponse, Integer> {
    InvoiceResponse createResponse(Integer userId, InvoiceResponseDTO dto);
}
