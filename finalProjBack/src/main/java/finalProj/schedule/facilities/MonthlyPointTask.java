package finalProj.schedule.facilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import finalProj.service.facilities.PointAccountsService;

@Component
public class MonthlyPointTask {
	@Autowired
	private PointAccountsService pointAccountsService;

	// 每月 1 號 00:00 執行
	@Scheduled(cron = "0 0 0 1 * ?")
	public void runMonthlyPointReset() {
		pointAccountsService.handleMonthlyPointReset();
	}
}
