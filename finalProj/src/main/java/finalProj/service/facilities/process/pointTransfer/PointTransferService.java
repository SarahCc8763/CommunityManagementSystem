package finalProj.service.facilities.process.pointTransfer;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.facilities.PointAccountsBean;
import finalProj.domain.facilities.PointTransactionsBean;
import finalProj.domain.users.Units;
import finalProj.dto.facilities.transfer.PointTransferRequest;
import finalProj.exception.facilities.ResourceNotFoundException;
import finalProj.repository.users.UnitsRepository;
import finalProj.service.facilities.PointAccountsService;
import finalProj.service.facilities.PointTransactionsService;

@Service
public class PointTransferService {

	@Autowired
	private PointAccountsService pointAccountsService;

	@Autowired
	private PointTransactionsService pointTransactionsService;

	@Autowired
	private UnitsRepository unitsRepository;

	
	public String getFirstUserNameByUnitId(Integer unitId) {
        Units unit = unitsRepository.findById(unitId)
            .orElseThrow(() -> new ResourceNotFoundException("找不到單位"));

        return unit.getUnitsUsersList().isEmpty()
            ? "未知使用者"
            : unit.getUnitsUsersList().get(0).getUser().getName();
    }	
	
	
	@Transactional
	public void transfer(PointTransferRequest request) {
		Integer fromUnitId = request.getFromUnitId();
		Integer toUnitId = request.getToUnitId();
		Integer amount = request.getAmount();

		if (amount == null || amount < 1) {
			throw new IllegalArgumentException("轉移點數必須大於 0");
		}

		// 取得來源與目標帳戶
		PointAccountsBean fromAccount = pointAccountsService.findByUnitId(fromUnitId);
		PointAccountsBean toAccount = pointAccountsService.findByUnitId(toUnitId);
		
		if (fromAccount == null || toAccount == null) {
		    throw new ResourceNotFoundException("找不到帳戶");
		}
		if (!fromAccount.getIsActive() || !toAccount.getIsActive()) {
		    throw new IllegalStateException("來源或目標帳戶已停用，無法轉移點數");
		}
		if (fromAccount.getTotalBalance() < amount) {
			throw new IllegalArgumentException("來源帳戶點數不足");
		}

		// 扣除來源點數
		fromAccount.setTotalBalance(fromAccount.getTotalBalance() - amount);		
		fromAccount.setUpdatedAt(LocalDateTime.now());
		pointAccountsService.update(fromAccount);		

		// 增加目標點數
		toAccount.setTotalBalance(toAccount.getTotalBalance() + amount);
		toAccount.setUpdatedAt(LocalDateTime.now());
		if (fromAccount.getExpiredAt().isAfter(toAccount.getExpiredAt())) {
		    toAccount.setExpiredAt(fromAccount.getExpiredAt());
		}
		pointAccountsService.update(toAccount);


		String toUserName = getFirstUserNameByUnitId(toUnitId);
		String fromUserName = getFirstUserNameByUnitId(fromUnitId);
		
		
		
		// 建立交易紀錄 - 來源單位
		PointTransactionsBean fromTxn = new PointTransactionsBean();
		fromTxn.setUnit(fromAccount.getUnit());
		fromTxn.setCommunity(fromAccount.getUnit().getCommunity());
		fromTxn.setAmount(-amount);
		fromTxn.setTransactionType("TRANSFER_OUT");
		fromTxn.setTransactionDescription("轉出至 " + toUserName);
		fromTxn.setRelatedUnitId(toUnitId);
		fromTxn.setCreatedAt(LocalDateTime.now());
		pointTransactionsService.create(fromTxn);

		// 建立交易紀錄 - 目標單位
		PointTransactionsBean toTxn = new PointTransactionsBean();
		toTxn.setUnit(toAccount.getUnit());
		toTxn.setCommunity(toAccount.getUnit().getCommunity());
		toTxn.setAmount(amount);
		toTxn.setTransactionType("TRANSFER_IN");
		toTxn.setTransactionDescription("由 " + fromUserName + " 轉入");		
		toTxn.setRelatedUnitId(fromUnitId);
		toTxn.setCreatedAt(LocalDateTime.now());
		pointTransactionsService.create(toTxn);
	}

}
