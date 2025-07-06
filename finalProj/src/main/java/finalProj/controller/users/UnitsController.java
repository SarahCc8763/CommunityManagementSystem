package finalProj.controller.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.users.Units;
import finalProj.dto.parking.ApiResponse;
import finalProj.repository.users.UnitsRepository;

@RestController
@RequestMapping("/units")
public class UnitsController {

    @Autowired
    UnitsRepository unitsRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Units>>> findByCommunity(@RequestParam("communityId") Integer communityId) {
        List<Units> units = unitsRepository.findByCommunity_CommunityId(communityId);
        return ResponseEntity.ok(ApiResponse.success("查詢成功", units));
    }
}
