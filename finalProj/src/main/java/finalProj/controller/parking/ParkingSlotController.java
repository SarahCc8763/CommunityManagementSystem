package finalProj.controller.parking;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.parking.ParkingSlot;
import finalProj.dto.parking.ApiResponse;
import finalProj.dto.parking.ParkingSlotDTO;
import finalProj.dto.parking.ParkingSlotQueryDTO;
import finalProj.repository.parking.ParkingSlotRepository;
import finalProj.service.parking.ParkingSlotService;

// 車位資料 Controller
@RestController
@RequestMapping("/park/parking-slots")
public class ParkingSlotController {

    @Autowired
    private ParkingSlotService service;

    @Autowired
    private ParkingSlotRepository repository;

    // 查詢單一車位資料
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ParkingSlotDTO>> getSlotById(@PathVariable("id") Integer id) {
        Optional<ParkingSlot> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.failure("查無此車位 ID"));
        }

        ParkingSlot slot = optional.get();
        ParkingSlotDTO dto = new ParkingSlotDTO();
        dto.setId(slot.getId());
        dto.setSlotNumber(slot.getSlotNumber());
        dto.setLocation(slot.getLocation());
        dto.setIsRentable(slot.getIsRentable());
        dto.setLicensePlate(slot.getLicensePlate());

        if (slot.getParkingType() != null) {
            dto.setParkingTypeId(slot.getParkingType().getId());
            dto.setParkingTypeName(slot.getParkingType().getType());
        }

        if (slot.getUsers() != null) {
            dto.setUsersId(slot.getUsers().getUsersId());
            dto.setUserName(slot.getUsers().getName());
        }

        return ResponseEntity.ok(ApiResponse.success("查詢成功", dto));
    }

    // 查詢某社區所有車位資料
    @GetMapping
    public ResponseEntity<ApiResponse<List<ParkingSlotDTO>>> getAll(@RequestParam("communityId") Integer communityId) {
        List<ParkingSlot> slots = repository.findByCommunity_CommunityId(communityId);

        if (slots == null || slots.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success("無車位資料", List.of())); // 回傳空 List，而不是 entity
        }

        List<ParkingSlotDTO> dtoList = slots.stream().map(slot -> {
            ParkingSlotDTO dto = new ParkingSlotDTO();
            dto.setId(slot.getId());
            dto.setSlotNumber(slot.getSlotNumber());
            dto.setLocation(slot.getLocation());
            dto.setIsRentable(slot.getIsRentable());
            dto.setLicensePlate(slot.getLicensePlate());

            if (slot.getParkingType() != null) {
                dto.setParkingTypeId(slot.getParkingType().getId());
                dto.setParkingTypeName(slot.getParkingType().getType());
            }

            if (slot.getUsers() != null) {
                dto.setUsersId(slot.getUsers().getUsersId());
                dto.setUserName(slot.getUsers().getName());
            }

            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("查詢成功", dtoList));
    }

    // 查詢某使用者所有車位資料
    @GetMapping("/user")
    public ResponseEntity<ApiResponse<List<ParkingSlotDTO>>> getSlotByUser(@RequestParam("usersId") Integer usersId) {
        List<ParkingSlot> slots = repository.findByUsers_UsersId(usersId);

        if (slots == null || slots.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success("無車位資料", List.of())); // 回傳空 List，而不是 entity
        }

        List<ParkingSlotDTO> dtoList = slots.stream().map(slot -> {
            ParkingSlotDTO dto = new ParkingSlotDTO();
            dto.setId(slot.getId());
            dto.setSlotNumber(slot.getSlotNumber());
            dto.setLocation(slot.getLocation());
            dto.setIsRentable(slot.getIsRentable());
            dto.setLicensePlate(slot.getLicensePlate());

            if (slot.getParkingType() != null) {
                dto.setParkingTypeId(slot.getParkingType().getId());
                dto.setParkingTypeName(slot.getParkingType().getType());
            }

            if (slot.getUsers() != null) {
                dto.setUsersId(slot.getUsers().getUsersId());
                dto.setUserName(slot.getUsers().getName());
            }

            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("查詢成功", dtoList));
    }

    // 新增車位資料 (多筆)
    @PostMapping("/batch")
    public ResponseEntity<ApiResponse<List<ParkingSlot>>> createAll(@RequestBody List<ParkingSlotDTO> dtos,
            @RequestParam("communityId") Integer communityId) {

        List<ParkingSlot> result = service.createAllFromDto(dtos, communityId);
        if (result == null || result.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.failure("資料格式錯誤或新增重複車位"));
        }
        return ResponseEntity.ok(ApiResponse.success("批次新增成功", result));
    }

    // 新增車位資料 (單筆)
    @PostMapping
    public ResponseEntity<ApiResponse<ParkingSlot>> create(@RequestBody ParkingSlotDTO dto,
            @RequestParam("communityId") Integer communityId) {
        ParkingSlot result = service.create(dto, communityId);
        if (result == null) {
            return ResponseEntity.badRequest().body(ApiResponse.failure("資料格式錯誤或新增重複車位"));
        }
        return ResponseEntity.ok(ApiResponse.success("新增成功", result));
    }

    // 修改車位資料
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ParkingSlot>> update(@RequestBody ParkingSlotDTO dto,
            @PathVariable("id") Integer id) {
        dto.setId(id);
        ParkingSlot result = service.update(dto);
        if (result == null) {
            return ResponseEntity.badRequest().body(ApiResponse.failure("修改失敗，請檢查資料"));
        }
        return ResponseEntity.ok(ApiResponse.success("修改成功", result));
    }

    // 查詢可抽籤車位
    @PostMapping("/available")
    public ResponseEntity<ApiResponse<List<ParkingSlot>>> getAvailableSlots(@RequestBody ParkingSlotQueryDTO dto) {
        List<ParkingSlot> result = service.findAvailableSlots(dto);
        if (result == null) {
            return ResponseEntity.badRequest().body(ApiResponse.failure("查詢參數不正確，請確認社區、類型與時間區間"));
        }
        return ResponseEntity.ok(ApiResponse.success("查詢成功", result));
    }

    // 刪除車位資料
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("id") Integer id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.success("刪除成功", null));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.failure("查無此車位 ID"));
    }
}
