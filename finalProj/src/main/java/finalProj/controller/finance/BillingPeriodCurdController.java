package finalProj.controller.finance;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import finalProj.domain.finance.BillingPeriod;
import finalProj.domain.finance.FeeType;
import finalProj.dto.finance.BillingPeriodDTO;
import finalProj.repository.finance.FeeTypeRepository;
import finalProj.service.finance.baseServiceInterfaces.BillingPeriodService;

@RestController
@RequestMapping("/finance/billing-periods")
public class BillingPeriodCurdController {

    @Autowired
    private BillingPeriodService billingPeriodService;

    @Autowired
    private FeeTypeRepository feeTypeRepository;

    // 取得全部
    @GetMapping
    public List<BillingPeriod> getAll() {
        return billingPeriodService.findAll();
    }

    // 用ID或是Code查詢
    @GetMapping("/query")
    public BillingPeriod queryByIdOrCode(
            @RequestParam(required = false) Integer billingPeriodId,
            @RequestParam(required = false) String periodCode) {
        if (billingPeriodId != null) {
            return billingPeriodService.findById(billingPeriodId);
        } else if (periodCode != null) {
            return billingPeriodService.findByPeriodCode(periodCode);
        } else {
            throw new IllegalArgumentException("請提供 billingPeriodId 或 periodCode 查詢");
        }
    }

    // 修改
    @PutMapping("/{id}")
    public BillingPeriod update(@PathVariable Integer id, @RequestBody BillingPeriodDTO dto) {
        BillingPeriod existing = billingPeriodService.findById(id);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到對應期別 ID: " + id);
        }

        // 更新欄位（排除 PK）
        existing.setPeriodName(dto.getPeriodName());
        existing.setPeriodCode(dto.getPeriodCode());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        existing.setDueDate(dto.getDueDate());
        existing.setNote(dto.getNote());
        existing.setStatus(dto.getStatus());
        existing.setCommunityId(dto.getCommunityId());
        existing.setUpdatedBy(dto.getUpdatedBy());
        existing.setLastUpdated(LocalDateTime.now());

        // 若 FeeType 有傳入 ID，則更新關聯
        if (dto.getFeeTypeId() != null) {
            FeeType feeType = feeTypeRepository.findById(dto.getFeeTypeId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "無效的費用類別 ID"));
            existing.setFeeType(feeType);
        }

        return billingPeriodService.save(existing);
    }

    @PostMapping("/create")
    public BillingPeriod createBillingPeriod(@RequestBody BillingPeriod billingPeriod) {
        BillingPeriod existing = billingPeriodService.findByPeriodCode(billingPeriod.getPeriodCode());
        if (existing != null) {
            return null; // 或者丟出例外、回傳錯誤訊息視需求 到時候看看前端想怎麼解決
        }
        return billingPeriodService.save(billingPeriod);
    }

    // 刪除
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        billingPeriodService.deleteById(id);
    }
}
