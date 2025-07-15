package finalProj.controller.packages;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import finalProj.domain.packages.Packages;
import finalProj.dto.packages.PackagesDTO;
import finalProj.dto.packages.PackagesSearchDTO;
import finalProj.dto.ticket.ApiResponse;
import finalProj.service.packages.PackagesService;

@RestController
@RequestMapping("/packages")
public class PackagesController {

    @Autowired
    PackagesService packagesService;

    // 新增包裹
    @PostMapping
    public ApiResponse<Packages> addPackage(@RequestBody Map<String, String> body) {
        try {
            Packages saved = packagesService.addPackages(body);
            return new ApiResponse<>(true, "包裹建立成功", saved);
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        }
    }

    // 查詢所有包裹
    @GetMapping("/unit/{unitId}")
    public List<Packages> getPackagesByUnit(@PathVariable Integer unitId) {
        return packagesService.findPackagesByUnitId(unitId);
    }

    // 管理員查包裹
    @PostMapping("/unit")
    public List<Packages> getPackagesByUnitSecurity(@RequestBody Map<String, String> body) {
        String unit = body.get("unit");
        String floor = body.get("floor");
        List<Packages> packages = packagesService.findPackagesByUnitIdSecurity(unit, floor);
        if (packages == null || packages.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "查無符合條件的包裹！unit=" + unit + ", floor=" + floor);

        }
        return packages;
    }

    // 多條件查詢（登入者限定自己的 unitId）
    @PostMapping("/search")
    public ApiResponse<List<Packages>> searchPackages(@RequestBody PackagesSearchDTO dto) {
        try {
            Integer unitId = dto.getUnitId(); // 從前端傳進來的 unitId
            List<Packages> result = packagesService.findPackagesByUnitId(unitId);
            return new ApiResponse<>(true, "查詢成功", result);
        } catch (Exception e) {
            return new ApiResponse<>(false, "查詢失敗：" + e.getMessage(), null);
        }
    }

    // 改變包裹狀態
    @PutMapping("/pickup/{id}")
    public ApiResponse<Void> markAsPickedUp(@PathVariable("id") Integer id) {
        try {
            packagesService.markAsPickedUp(id);
            return new ApiResponse<>(true, "已領取", null);
        } catch (Exception e) {
            return new ApiResponse<>(false, "更新失敗：" + e.getMessage(), null);
        }
    }

    // 修改
    @PutMapping("/update/{id}")
    public ApiResponse<Packages> update(@PathVariable("id") Integer id, @RequestBody PackagesDTO dto) {
        try {
            Packages result = packagesService.update(id, dto);
            return new ApiResponse<>(true, "更新成功", result);
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        }
    }

}