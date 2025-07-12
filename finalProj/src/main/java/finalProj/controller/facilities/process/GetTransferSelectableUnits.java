package finalProj.controller.facilities.process;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.users.Units;
import finalProj.dto.facilities.transfer.UnitOptionDTO;
import finalProj.repository.users.UnitsRepository;
@CrossOrigin
@RestController
@RequestMapping("/api/units")
public class GetTransferSelectableUnits {
	
	@Autowired
	UnitsRepository unitsRepository;
	
	@GetMapping("/selectable")
	public List<UnitOptionDTO> getSelectableUnits(
	        @RequestParam(required = false, defaultValue = "1") Integer communityId,
	        @RequestParam Integer excludeUnitId) {

	    if (communityId == null) {
	        communityId = 1; // 預設值
	    }
	    
	    List<Units> units = unitsRepository.findByCommunityCommunityIdAndUnitsIdNot(communityId, excludeUnitId);
	                                        
	    return units.stream()
	    	    .sorted(Comparator
	    	        .comparing(Units::getUnit)
	    	        .thenComparing(Units::getFloor))
	    	    .map(unit -> {
	    	        String userName = unit.getUnitsUsersList() != null && !unit.getUnitsUsersList().isEmpty()
	    	            ? unit.getUnitsUsersList().get(0).getUser().getName()
	    	            : "—"; // 預設名稱

	    	        return new UnitOptionDTO(
	    	            unit.getUnitsId(),
	    	            unit.getUnit() + "-" + unit.getFloor(),
	    	            userName
	    	        );
	    	    })
	    	    .collect(Collectors.toList());
	}
}
