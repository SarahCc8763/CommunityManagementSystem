package finalProj.controller.finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.finance.Invoice;
import finalProj.dto.finance.FeeGenerationRequest;
import finalProj.service.finance.baseServiceInterfaces.InvoiceGeneratingService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/finance/invoice-generator")
public class InvoiceGeneratingController {

    @Autowired
    private InvoiceGeneratingService invoiceGeneratingService;

    // @PostMapping("/generate")
    // public ResponseEntity<?> generateInvoices(@RequestBody FeeGenerationRequest
    // request) {
    // try {
    // invoiceGeneratingService.generateInvoices(
    // request.getFeeTypeId(),
    // request.getBillingPeriodId());
    // return ResponseEntity.ok("已成功產生帳單");
    // } catch (Exception e) {
    // return ResponseEntity.badRequest().body("產生失敗：" + e.getMessage());
    // }
    // }

    @PostMapping("/generate")
    public String generateInvoices(@RequestBody Invoice request) {
        try {
            Boolean result = invoiceGeneratingService.generateInvoices(request.getFeeType().getFeeTypeId(),
                    request.getBillingPeriod().getBillingPeriodId(), request.getCreatedBy());
            if (result) {

                return "繳費單產生成功";
            }
            return "繳費單產生失敗";
        } catch (Exception e) {
            return "繳費單產生失敗：" + e.getMessage();
        }
    }

    @PostMapping("/batch-generate")
    public ResponseEntity<?> batchGenerateInvoices(@RequestBody BatchGenerateRequest request) {
        try {
            invoiceGeneratingService.batchGenerateInvoices(
                    request.getBillingPeriodId(),
                    request.getFeeTypeId(),
                    request.getUserIds(),
                    request.getCreatedBy());
            return ResponseEntity.ok("批次產生成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("批次產生失敗：" + e.getMessage());
        }
    }

    // DTO
    public static class BatchGenerateRequest {
        private Integer billingPeriodId;
        private Integer feeTypeId;
        private List<Integer> userIds;
        private Integer createdBy;

        public Integer getBillingPeriodId() {
            return billingPeriodId;
        }

        public void setBillingPeriodId(Integer billingPeriodId) {
            this.billingPeriodId = billingPeriodId;
        }

        public Integer getFeeTypeId() {
            return feeTypeId;
        }

        public void setFeeTypeId(Integer feeTypeId) {
            this.feeTypeId = feeTypeId;
        }

        public List<Integer> getUserIds() {
            return userIds;
        }

        public void setUserIds(List<Integer> userIds) {
            this.userIds = userIds;
        }

        public Integer getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Integer createdBy) {
            this.createdBy = createdBy;
        }
    }
}
