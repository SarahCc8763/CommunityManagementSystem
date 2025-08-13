package finalProj.service.finance.baseServiceInterfaces;

import finalProj.domain.finance.Invoice;
import finalProj.domain.finance.InvoiceResponse;
import finalProj.dto.finance.InvoiceDTO;
import finalProj.dto.finance.InvoiceResponseDTO;
import java.util.List;

import org.springframework.data.repository.query.Param;

public interface InvoiceResponseService extends BaseService<InvoiceResponse, Integer> {
    InvoiceResponseDTO createResponse(Integer userId, InvoiceResponse dto);

    List<InvoiceResponseDTO> findByUserId(Integer userId);

    List<InvoiceResponseDTO> findAllDTO();

    InvoiceResponseDTO findDTOById(Integer id);

    InvoiceResponseDTO verifyResponse(Integer id, Integer adminId);

    List<InvoiceResponseDTO> findByInvoiceId(Integer invoiceId);

    List<InvoiceDTO> findUnpaidInvoiceWithResponse();

    List<InvoiceResponseDTO> findUnpaidInvoiceResponse();

    List<InvoiceDTO> findUnpaidInvoiceWithResponseByCommunityId(@Param("communityId") Integer communityId);

}
