package finalProj.controller.finance;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
import finalProj.dto.finance.FeeTypeDTO;
import finalProj.repository.finance.FeeTypeRepository;
import finalProj.service.finance.baseServiceInterfaces.BillingPeriodService;

@RestController
@RequestMapping("/finance/billing-periods")
public class BillingPeriodCurdController {

    @Autowired
    private BillingPeriodService billingPeriodService;

    @Autowired
    private FeeTypeRepository feeTypeRepository;

    // Entity 轉 DTO
    private BillingPeriodDTO toDTO(BillingPeriod entity) {
        if (entity == null)
            return null;
        BillingPeriodDTO dto = new BillingPeriodDTO();
        dto.setBillingPeriodId(entity.getBillingPeriodId());
        dto.setPeriodName(entity.getPeriodName());
        dto.setPeriodCode(entity.getPeriodCode());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setDueDate(entity.getDueDate());
        dto.setNote(entity.getNote());
        dto.setStatus(entity.getStatus());
        dto.setCommunityId(entity.getCommunityId());
        // 巢狀 FeeTypeDTO
        if (entity.getFeeType() != null) {
            FeeType feeType = entity.getFeeType();
            FeeTypeDTO feeTypeDTO = new FeeTypeDTO();
            feeTypeDTO.setFeeTypeId(feeType.getFeeTypeId());
            feeTypeDTO.setDescription(feeType.getDescription());
            feeTypeDTO.setFeeCode(feeType.getFeeCode());
            feeTypeDTO.setAmountPerUnit(feeType.getAmountPerUnit());
            feeTypeDTO.setFrequency(feeType.getFrequency());
            feeTypeDTO.setUnit(feeType.getUnit());
            feeTypeDTO.setNote(feeType.getNote());
            feeTypeDTO.setCommunityId(feeType.getCommunityId());
            feeTypeDTO.setStatus(feeType.getStatus());
            dto.setFeeType(feeTypeDTO);
        }
        return dto;
    }

    // DTO 轉 Entity（for create/update）
    private BillingPeriod toEntity(BillingPeriodDTO dto) {
        BillingPeriod entity = new BillingPeriod();
        entity.setBillingPeriodId(dto.getBillingPeriodId());
        entity.setPeriodName(dto.getPeriodName());
        entity.setPeriodCode(dto.getPeriodCode());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setDueDate(dto.getDueDate());
        entity.setNote(dto.getNote());
        entity.setStatus(dto.getStatus());
        entity.setCommunityId(dto.getCommunityId());
        // FeeType 關聯
        if (dto.getFeeType() != null && dto.getFeeType().getFeeTypeId() != null) {
            FeeType feeType = feeTypeRepository.findById(dto.getFeeType().getFeeTypeId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "無效的費用類別 ID"));
            entity.setFeeType(feeType);
        }
        return entity;
    }

    // 【功能】取得所有繳費期別
    @GetMapping
    public List<BillingPeriodDTO> getAll() {
        return billingPeriodService.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    // 【功能】依ID或期別代碼查詢單一繳費期別
    @GetMapping("/query")
    public BillingPeriodDTO queryByIdOrCode(
            @RequestParam(required = false) Integer billingPeriodId,
            @RequestParam(required = false) String periodCode) {
        BillingPeriod entity = null;
        if (billingPeriodId != null) {
            entity = billingPeriodService.findById(billingPeriodId);
        } else if (periodCode != null) {
            entity = billingPeriodService.findByPeriodCode(periodCode);
        } else {
            throw new IllegalArgumentException("請提供 billingPeriodId 或 periodCode 查詢");
        }
        return toDTO(entity);
    }

    // 【功能】更新繳費期別內容
    @PutMapping("/{id}")
    public BillingPeriodDTO update(@PathVariable Integer id, @RequestBody BillingPeriodDTO dto) {
        BillingPeriod existing = billingPeriodService.findById(id);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到對應期別 ID: " + id);
        }
        // 更新欄位
        existing.setPeriodName(dto.getPeriodName());
        existing.setPeriodCode(dto.getPeriodCode());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        existing.setDueDate(dto.getDueDate());
        existing.setNote(dto.getNote());
        existing.setStatus(dto.getStatus());
        existing.setCommunityId(dto.getCommunityId());
        existing.setLastUpdated(LocalDateTime.now());
        // FeeType 關聯
        if (dto.getFeeType() != null && dto.getFeeType().getFeeTypeId() != null) {
            FeeType feeType = feeTypeRepository.findById(dto.getFeeType().getFeeTypeId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "無效的費用類別 ID"));
            existing.setFeeType(feeType);
        }
        BillingPeriod saved = billingPeriodService.save(existing);
        return toDTO(saved);
    }

    // 【功能】建立新繳費期別
    @PostMapping("/create")
    public BillingPeriodDTO createBillingPeriod(@RequestBody BillingPeriodDTO dto) {
        // 檢查 periodCode 是否重複
        BillingPeriod existing = billingPeriodService.findByPeriodCode(dto.getPeriodCode());
        if (existing != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "期別代碼已存在");
        }
        BillingPeriod entity = toEntity(dto);
        BillingPeriod saved = billingPeriodService.save(entity);
        return toDTO(saved);
    }

    // 【功能】刪除繳費期別
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        billingPeriodService.deleteById(id);
    }
}
