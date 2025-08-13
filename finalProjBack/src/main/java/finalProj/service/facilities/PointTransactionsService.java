package finalProj.service.facilities;

import java.util.List;

import finalProj.domain.facilities.PointTransactionsBean;
import finalProj.dto.facilities.reservations.PointTransactionsRecordDTO;

public interface PointTransactionsService {
	
	// 新增一筆異動紀錄（加點、扣點、轉移）
	PointTransactionsBean create(PointTransactionsBean transaction);

	// 查詢某位住戶所有點數異動紀錄（含儲值、扣點、轉帳等）
	List<PointTransactionsBean> findByUnit_UnitsId(Integer unitId);
	
	List<PointTransactionsRecordDTO> findDTOsByUnitId(Integer unitId);
}
