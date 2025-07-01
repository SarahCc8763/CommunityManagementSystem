package finalProj.controller.packages;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

	//新增包裹
	@PostMapping
	public ApiResponse<Packages> addPackage(@RequestBody PackagesDTO dto) {
		try {
			Packages saved = packagesService.addPackages(dto);
			return new ApiResponse<>(true, "包裹建立成功", saved);
		} catch (IllegalArgumentException e) {
			return new ApiResponse<>(false, e.getMessage(), null);
		}
	}
	
	//查詢所有包裹
	@GetMapping("/unit/{unitId}")
	public List<Packages> getPackagesByUnit(@PathVariable Integer unitId) {
	    return packagesService.findPackagesByUnitId(unitId);
	}
	
	
	
    // 多條件查詢（登入者限定自己的 unitId）
    @PostMapping("/search")
    public ApiResponse<List<Packages>> searchPackages(@RequestBody PackagesSearchDTO dto) {
        try {
            // 模擬從登入資訊中取得目前使用者的 unitId
            Integer loginUnitId = getLoginUnitId(); // ⚠️ 請替換為實際取得登入者 unitId 的邏輯

            List<Packages> result = packagesService.search(dto, loginUnitId);
            return new ApiResponse<>(true, "查詢成功", result);
        } catch (Exception e) {
            return new ApiResponse<>(false, "查詢失敗：" + e.getMessage(), null);
        }
    }

    // 假的登入資訊（請改為從 session 或 JWT 中取得）
    private Integer getLoginUnitId() {
        return 3; // 假設登入者是 unitId = 3
    }
    
    //改變包裹狀態
    @PutMapping("/pickup/{id}")
    public ApiResponse<Void> markAsPickedUp(@PathVariable("id") Integer id) {
        try {
            packagesService.markAsPickedUp(id);
            return new ApiResponse<>(true, "已領取", null);
        } catch (Exception e) {
            return new ApiResponse<>(false, "更新失敗：" + e.getMessage(), null);
        }
    }
    
    //修改
    @PutMapping("/update/{id}")
    public ApiResponse<Packages> update(@PathVariable("id") Integer id, @RequestBody PackagesDTO dto){
		try {
			Packages result = packagesService.update(id, dto);
			return new ApiResponse<>(true, "更新成功", result);
		} catch (IllegalArgumentException e) {
			return new ApiResponse<>(false, e.getMessage(), null);
		}
    }
    
}
