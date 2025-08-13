package finalProj.service.facilities;

import java.time.LocalDateTime;

import finalProj.domain.facilities.PointAccountsBean;

public interface PointAccountsService {

	PointAccountsBean findById(Integer id);

	PointAccountsBean findByUnitId(Integer id);

	PointAccountsBean decreasePoints(PointAccountsBean account, Integer deductAmount);

	PointAccountsBean increasePoints(PointAccountsBean account, int refundAmount, LocalDateTime refundExpireAt);

	PointAccountsBean update(PointAccountsBean account);

	// 排程，每月初檢查expire並增發點數
	void handleMonthlyPointReset();
}
