package finalProj.controller.facilities.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProj.service.facilities.PointAccountsService;
@CrossOrigin
@RestController
@RequestMapping("/api/dev-tools")
public class DevToolsController {

    @Autowired
    private PointAccountsService pointAccountsService;

    // 手動執行每月點數邏輯（測試用）
    @PostMapping("/run-monthly-reset")
    public ResponseEntity<String> runMonthlyPointResetNow() {
        pointAccountsService.handleMonthlyPointReset();
        return ResponseEntity.ok("已手動執行每月點數邏輯");
    }
}
