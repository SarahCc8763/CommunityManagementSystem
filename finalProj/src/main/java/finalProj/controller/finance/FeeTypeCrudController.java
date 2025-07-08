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

import finalProj.domain.finance.FeeType;
import finalProj.dto.finance.FeeTypeDTO;
import finalProj.service.finance.baseServiceInterfaces.FeeTypeService;

@RestController
@RequestMapping("/finance/fee-types")
public class FeeTypeCrudController {

    @Autowired
    private FeeTypeService feeTypeService;

    // Entity 轉 DTO
    private FeeTypeDTO toDTO(FeeType entity) {
        if (entity == null)
            return null;
        FeeTypeDTO dto = new FeeTypeDTO();
        dto.setFeeTypeId(entity.getFeeTypeId());
        dto.setFeeCode(entity.getFeeCode());
        dto.setDescription(entity.getDescription());
        dto.setAmountPerUnit(entity.getAmountPerUnit());
        dto.setUnit(entity.getUnit());
        dto.setFrequency(entity.getFrequency());
        dto.setNote(entity.getNote());
        dto.setStatus(entity.getStatus());
        dto.setCommunityId(entity.getCommunityId());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        return dto;
    }

    // DTO 轉 Entity
    private FeeType toEntity(FeeTypeDTO dto) {
        FeeType entity = new FeeType();
        entity.setFeeTypeId(dto.getFeeTypeId());
        entity.setFeeCode(dto.getFeeCode());
        entity.setDescription(dto.getDescription());
        entity.setAmountPerUnit(dto.getAmountPerUnit());
        entity.setUnit(dto.getUnit());
        entity.setFrequency(dto.getFrequency());
        entity.setNote(dto.getNote());
        entity.setStatus(dto.getStatus());
        entity.setCommunityId(dto.getCommunityId());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setUpdatedBy(dto.getUpdatedBy());
        return entity;
    }

    // 【功能】取得所有費用類型（可依communityId查詢）
    @GetMapping
    public List<FeeTypeDTO> getAll(@RequestParam(required = false) Integer communityId) {
        List<FeeType> feeTypes;
        if (communityId != null) {
            feeTypes = feeTypeService.findByCommunityId(communityId);
        } else {
            feeTypes = feeTypeService.findAll();
        }
        return feeTypes.stream().map(this::toDTO).toList();
    }

    // 【功能】依ID查詢單一費用類型
    @GetMapping("/{id}")
    public FeeTypeDTO getById(@PathVariable Integer id) {
        FeeType entity = feeTypeService.findById(id);
        if (entity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到費用類別 ID: " + id);
        }
        return toDTO(entity);
    }

    // 【功能】建立新費用類型
    @PostMapping
    public FeeTypeDTO create(@RequestBody FeeTypeDTO dto) {
        // 檢查 feeCode 是否重複
        List<FeeType> all = feeTypeService.findAll();
        if (all.stream().anyMatch(f -> f.getFeeCode().equals(dto.getFeeCode()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "費用代碼已存在");
        }
        FeeType feeType = toEntity(dto);
        feeType.setCreatedAt(LocalDateTime.now());
        feeType.setLastUpdated(LocalDateTime.now());
        FeeType saved = feeTypeService.save(feeType);
        return toDTO(saved);
    }

    // 【功能】更新費用類型內容
    @PutMapping("/{id}")
    public FeeTypeDTO update(@PathVariable Integer id, @RequestBody FeeTypeDTO dto) {
        FeeType existing = feeTypeService.findById(id);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到費用類別 ID: " + id);
        }
        // 只更新有傳入的欄位
        if (dto.getDescription() != null)
            existing.setDescription(dto.getDescription());
        if (dto.getNote() != null)
            existing.setNote(dto.getNote());
        if (dto.getStatus() != null)
            existing.setStatus(dto.getStatus());
        if (dto.getCommunityId() != null)
            existing.setCommunityId(dto.getCommunityId());
        if (dto.getUpdatedBy() != null)
            existing.setUpdatedBy(dto.getUpdatedBy());
        existing.setLastUpdated(LocalDateTime.now());
        FeeType saved = feeTypeService.save(existing);
        return toDTO(saved);
    }

    // 【功能】刪除費用類型
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        feeTypeService.deleteById(id);
    }
}