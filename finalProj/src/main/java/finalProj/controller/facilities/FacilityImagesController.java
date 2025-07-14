package finalProj.controller.facilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.facilities.FacilitiesBean;
import finalProj.domain.facilities.FacilityImagesBean;
import finalProj.exception.facilities.ResourceNotFoundException;
import finalProj.repository.facilities.FacilitiesRepository;
import finalProj.repository.facilities.FacilityImagesRepository;
import finalProj.service.facilities.FacilityImagesService;

@RestController
@RequestMapping("/api/facility-images")
public class FacilityImagesController {
	
	@Autowired
    private FacilityImagesService facilityImagesService;

    @Autowired
    private FacilitiesRepository facilitiesRepository;
    
    @Autowired
    private FacilityImagesRepository facilityImagesRepository;

    // 上傳圖片
    @PostMapping
    public ResponseEntity<FacilityImagesBean> uploadImage(@RequestBody FacilityImagesBean image) {
        Integer facilityId = image.getFacility() != null ? image.getFacility().getFacilityId() : null;
        if (facilityId == null) {
            throw new IllegalArgumentException("facilityId 不可為空");
        }

        FacilitiesBean facility = facilitiesRepository.findById(facilityId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到對應公設"));

        image.setFacility(facility);
        FacilityImagesBean saved = facilityImagesService.save(image);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // 查詢某公設所有圖片
    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<List<FacilityImagesBean>> getImagesByFacility(@PathVariable Integer facilityId) {
        return ResponseEntity.ok(facilityImagesService.findByFacilityId(facilityId));
    }

    @GetMapping("/view/facility/{facilityId}")
    public ResponseEntity<byte[]> viewFacilityImage(@PathVariable Integer facilityId) throws IOException {
        // 1. 從資料庫查第一張圖片
        FacilityImagesBean image = facilityImagesRepository
            .findFirstByFacilityFacilityIdOrderBySortOrderAsc(facilityId)
            .orElseThrow(() -> new ResourceNotFoundException("找不到圖片"));

        // 2. 從 image.getImageUrl() 取得實體檔案名稱（如 pic1.jpg）
        String filename = Paths.get(image.getImageUrl()).getFileName().toString();

        // 3. 自動組成路徑（建議從 application.properties 讀）
        String baseDir = System.getProperty("facility.image.base", "C:/uploads/images/facilities/");
        Path path = Paths.get(baseDir, filename);

        // 4. 讀檔回傳
        byte[] imageBytes = Files.readAllBytes(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // or 自動判斷副檔名

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
    
    
    // 刪除圖片
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Integer imageId) {
        facilityImagesService.deleteById(imageId);
        return ResponseEntity.noContent().build();
    }
}
