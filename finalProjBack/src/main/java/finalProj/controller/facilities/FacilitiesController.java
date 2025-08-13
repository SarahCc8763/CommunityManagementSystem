package finalProj.controller.facilities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.community.Community;
import finalProj.domain.facilities.FacilitiesBean;
import finalProj.dto.facilities.facilities.CreateFacilityRequest;
import finalProj.dto.facilities.facilities.FacilitySimpleDTO;
import finalProj.dto.facilities.facilities.UpdateFacilityNameRequest;
import finalProj.exception.facilities.ResourceNotFoundException;
import finalProj.repository.community.CommunityRepository;
import finalProj.service.facilities.FacilitiesService;
import jakarta.validation.Valid;
@CrossOrigin
@RestController
@RequestMapping("/api/facilities")
public class FacilitiesController {
	
	@Autowired
	private FacilitiesService facilitiesService;

	@Autowired
	private CommunityRepository communityRepository;
	         
	// 新增公設	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FacilitiesBean> createFacility(@Valid @RequestBody CreateFacilityRequest request) {

	    // 查社區
	    Community community = communityRepository.findById(request.getCommunityId())
	        .orElseThrow(() -> new ResourceNotFoundException("找不到指定的社區"));

	    // 將 request 轉為 entity
	    FacilitiesBean facility = new FacilitiesBean();
	    facility.setCommunity(community);
	    facility.setFacilityName(request.getFacilityName());
	    facility.setMaxUsers(request.getMaxUsers());
	    facility.setFacilityDescription(request.getFacilityDescription());
	    facility.setFacilityLocation(request.getFacilityLocation());
	    facility.setOpenTime(request.getOpenTime());
	    facility.setCloseTime(request.getCloseTime());
	    facility.setReservableDuration(request.getReservableDuration());
	    facility.setFee(request.getFee());
	    facility.setCreatedAt(LocalDateTime.now());
	    facility.setFacilityStatus("active");

	    FacilitiesBean saved = facilitiesService.create(facility);
	    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}
	
	// 查詢公設（可加 communityId 過濾）
	@GetMapping
	public ResponseEntity<List<FacilitiesBean>> getFacilities(@RequestParam(name = "communityId", required = false) Integer communityId) {
	    
		List<FacilitiesBean> facilities;

		if (communityId != null) {
			facilities = facilitiesService.findByCommunityCommunityIdAndFacilityStatus(communityId, "active");
		} else {
			facilities = facilitiesService.findByFacilityStatus("active");
		}

	    return ResponseEntity.ok(facilities);
	}
	
	// 修改公設
	@PutMapping("/{id}/name")
	public ResponseEntity<FacilitiesBean> updateFacilityName(
	        @PathVariable("id") Integer facilityId,
	        @RequestBody UpdateFacilityNameRequest request) {		
		FacilitiesBean existing = facilitiesService.findById(facilityId);
		existing.setFacilityName(request.getFacilityName());
		existing.setUpdatedAt(LocalDateTime.now());

		FacilitiesBean saved = facilitiesService.update(existing);
		return ResponseEntity.ok(saved);
	}
	
	//只列可預約的公設，內容包含facilityId,facilityName,facilityDescription,facilityLocation,fee,imageUrl
  
	@GetMapping("/simple")
	public List<FacilitySimpleDTO> getSimpleFacilities() {
	    return facilitiesService.findAll().stream()
	        .filter(f -> "active".equalsIgnoreCase(f.getFacilityStatus())) // 只取 active
	        .map(f -> {
	            FacilitySimpleDTO dto = new FacilitySimpleDTO();
	            dto.setFacilityId(f.getFacilityId());
	            dto.setFacilityName(f.getFacilityName());
	            dto.setFacilityDescription(f.getFacilityDescription());
	            dto.setFacilityLocation(f.getFacilityLocation());
	            dto.setFee(f.getFee());

	            // 如果有圖就用第一張，否則用預設圖
	            if (f.getImages() != null && 
	            	!f.getImages().isEmpty() &&
	            	f.getImages().get(0).getImageUrl() != null &&
	            	!f.getImages().get(0).getImageUrl().isEmpty()) {
	                dto.setImageUrl(f.getImages().get(0).getImageUrl());
	            } else {
	                dto.setImageUrl("/images/facilities/no-image.jpg");
	            }

	            return dto;
	        }).collect(Collectors.toList());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<FacilitiesBean> getSingleFacility(@PathVariable("id") Integer facilityId) {
	    FacilitiesBean facility = facilitiesService.findById(facilityId);
	    return (facility != null) 
	        ? ResponseEntity.ok(facility) 
	        : ResponseEntity.notFound().build();
	}
}
