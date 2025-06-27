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

    @GetMapping
    public List<FeeType> getAll() {
        return feeTypeService.findAll();
    }

    @GetMapping("/{id}")
    public FeeType getById(@PathVariable Integer id) {
        return feeTypeService.findById(id);
    }

    @PostMapping
    public FeeType create(@RequestBody FeeTypeDTO dto) {
        FeeType feeType = new FeeType();
        feeType.setDescription(dto.getDescription());
        feeType.setNote(dto.getNote());
        feeType.setStatus(dto.getStatus());
        feeType.setCommunityId(dto.getCommunityId());
        feeType.setCreatedBy(dto.getCreatedBy());
        feeType.setUpdatedBy(dto.getUpdatedBy());
        feeType.setCreatedAt(LocalDateTime.now());
        feeType.setLastUpdated(LocalDateTime.now());
        return feeTypeService.save(feeType);
    }

    @PutMapping("/{id}")
    public FeeType update(@PathVariable Integer id, @RequestBody FeeTypeDTO dto) {
        FeeType existing = feeTypeService.findById(id);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到費用類別 ID: " + id);
        }
        existing.setDescription(dto.getDescription());
        existing.setNote(dto.getNote());
        existing.setStatus(dto.getStatus());
        existing.setCommunityId(dto.getCommunityId());
        existing.setUpdatedBy(dto.getUpdatedBy());
        existing.setLastUpdated(LocalDateTime.now());
        return feeTypeService.save(existing);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        feeTypeService.deleteById(id);
    }
}