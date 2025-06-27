package finalProj.controller.parking;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import finalProj.domain.parking.ParkingRentals;
import finalProj.domain.parking.ParkingSlot;
import finalProj.dto.parking.RentalHistoryDTO;
import finalProj.repository.parking.ParkingRentalsRepository;
import finalProj.repository.parking.ParkingSlotRepository;
import finalProj.service.parking.ParkingRentalsService;

// 承租紀錄 Controller
@RestController
@RequestMapping("/park/parking-rentals")
public class ParkingRentalsController {

	@Autowired
	private ParkingRentalsService service;
	
	@Autowired
	private ParkingSlotRepository parkingSlotRepository;
	
	@Autowired
    private ParkingRentalsRepository parkingRentalsRepository;

	// 查詢某車位的承租歷史
	@GetMapping("/{slotId}/history")
    public List<RentalHistoryDTO> getRentalHistory(
        @PathVariable Integer slotId,
        @RequestParam String range
    ) {
        // 1. 找出該 slotNumber 對應的 slot id
        Optional<ParkingSlot> slotOpt = parkingSlotRepository.findById(slotId);
        if (slotOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "車位不存在");
        }


        // 2. 轉換 range 為 Date 範圍
        Date startDate = switch (range) {
            case "1" -> getDateYearsAgo(1);
            case "3" -> getDateYearsAgo(3);
            case "5" -> getDateYearsAgo(5);
            case "all" -> null;
            default -> throw new IllegalArgumentException("無效的範圍參數");
        };

        // 3. 查詢資料
        List<ParkingRentals> rentals = parkingRentalsRepository.findHistoryBySlotIdAndStartDate(slotId, startDate);

        // 4. 回傳前端格式
        return rentals.stream()
            .map(r -> new RentalHistoryDTO(
                r.getLicensePlate(),
                r.getRentBuyStart(),
                r.getRentEnd(),
                r.getStatus()
            ))
            .collect(Collectors.toList());
    }

    private Date getDateYearsAgo(int years) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -years);
        return cal.getTime();
    }
	
	// 查詢可承租車位
	@GetMapping("/available-slots")
	public List<ParkingSlot> findAvailableSlots(
	    @RequestParam Integer parkingTypeId,
	    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
	    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end) {
	    
	    return service.findAvailableSlots(parkingTypeId, start, end);
	}

	
	// 查詢所有承租紀錄
	@GetMapping
	public List<ParkingRentals> getAll() {
		return service.findAll();
	}
	
	// 驗證時段重疊
	@PostMapping("/overlap")
	public Boolean isOverlapping(@RequestBody ParkingRentals rental) {
		return service.isOverlapping(rental.getRentBuyStart(),rental.getRentEnd(),rental.getParkingSlotId(), rental.getId());
	}	

	// 新增承租紀錄
	@PostMapping
	public ParkingRentals create(@RequestBody ParkingRentals rental) {
		return service.create(rental);
	}

	// 修改承租紀錄
	@PutMapping("/{id}")
	public ParkingRentals update(@RequestBody ParkingRentals rental, @PathVariable Integer id) {
		rental.setId(id);
		return service.update(rental);
	}

	// 刪除承租紀錄
	@DeleteMapping("/{id}")
	public Boolean delete(@PathVariable Integer id) {
		return service.delete(id);
	}

}
