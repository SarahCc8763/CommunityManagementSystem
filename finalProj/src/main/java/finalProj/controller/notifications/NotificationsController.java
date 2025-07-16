package finalProj.controller.notifications;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.notifications.UnitsNotifications;
import finalProj.domain.users.Units;
import finalProj.repository.notifications.UnitsNotificationsRepository;
import finalProj.repository.users.UnitsRepository;

@RestController
@RequestMapping("/notifications")
public class NotificationsController {
	@Autowired
	  private UnitsRepository unitsRepository;

	  @Autowired
	  private UnitsNotificationsRepository unitsNotificationsRepository;

	  @GetMapping("/unit/{unitId}")
	  public ResponseEntity<?> getNotificationsByUnit(@PathVariable Integer unitId) {
	    Optional<Units> unitOpt = unitsRepository.findById(unitId);

	    if (unitOpt.isEmpty()) {
	      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
	        Map.of("success", false, "message", "找不到該門牌單位 unitId=" + unitId)
	      );
	    }

	    Units unit = unitOpt.get();

	    List<UnitsNotifications> unitsNotifications = unitsNotificationsRepository.findByUnitsUnitsId(unit.getUnitsId());

	    List<Map<String, String>> result = unitsNotifications.stream()
	      .map(un -> Map.of(
	    	"unitsNotificationsId",un.getUnitsNotificationsId().toString(),
	        "title", un.getNotifications().getTitle(),
	        "description", un.getNotifications().getDescription(),
	        "isRead",String.valueOf(un.getIsRead()),
	        "readTime",un.getReadTime() != null ? un.getReadTime().toString() : ""
	      ))
	      .collect(Collectors.toList());

	    return ResponseEntity.ok(Map.of(
	      "success", true,
	      "data", result
	    ));
	  }
	  
	  @PutMapping("/isRead/{unitsNotificationsId}")
	  public ResponseEntity<?> markNotificationAsRead(@PathVariable Integer unitsNotificationsId) {
//	    Integer unitsNotificationsId = body.get("unitsNotificationsId");

	    UnitsNotifications un = unitsNotificationsRepository.findById(unitsNotificationsId)
	      .orElseThrow(() -> new IllegalArgumentException("找不到通知"));

	    un.setIsRead(1);
	    un.setReadTime(LocalDateTime.now());

	    unitsNotificationsRepository.save(un);

	    return ResponseEntity.ok(Map.of(
	      "success", true,
	      "message", "通知已標記為已讀"
	    ));
	  }

}
