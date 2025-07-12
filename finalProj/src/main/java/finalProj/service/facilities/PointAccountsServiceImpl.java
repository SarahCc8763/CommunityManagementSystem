package finalProj.service.facilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.community.Community;
import finalProj.domain.facilities.PointAccountsBean;
import finalProj.domain.facilities.PointTransactionsBean;
import finalProj.domain.users.Units;
import finalProj.exception.facilities.InsufficientPointsException;
import finalProj.exception.facilities.ResourceNotFoundException;
import finalProj.repository.facilities.PointAccountsRepository;
import finalProj.repository.users.UnitsRepository;

@Service
@Transactional
public class PointAccountsServiceImpl implements PointAccountsService {

	@Autowired
	PointAccountsRepository pointAccountsRepository;
	
	@Autowired
	UnitsRepository unitsRepository;
	
	@Autowired
	PointTransactionsService pointTransactionsService;
	
	@Override
	public PointAccountsBean findById(Integer id) {
		// 使用 orElseThrow() 來拋出異常
		return pointAccountsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("找不到 ID 為 " + id + " 的帳戶。"));
	}
	
	@Override
	public PointAccountsBean update(PointAccountsBean account) {
		  return pointAccountsRepository.save(account); 
	}

	@Override
	public PointAccountsBean decreasePoints(PointAccountsBean account, Integer deductAmount) {
	    if (account == null) {
	        throw new IllegalArgumentException("帳戶不得為 null");
	    }

	    if (deductAmount == null || deductAmount < 0) {
	        throw new IllegalArgumentException("扣點數量應為正數");
	    }

	    int currentBalance = account.getTotalBalance();
	    if (currentBalance < deductAmount) {
	        throw new InsufficientPointsException("點數不足，扣點失敗");
	    }

	    // 扣除點數並更新時間
	    account.setTotalBalance(currentBalance - deductAmount);
	    account.setUpdatedAt(LocalDateTime.now());

	    return pointAccountsRepository.save(account);
	}

	@Override
	public PointAccountsBean findByUnitId(Integer unitId) {
        return pointAccountsRepository.findByUnit_UnitsId(unitId)
            .orElseThrow(() -> new ResourceNotFoundException("找不到該住戶的點數帳戶"));
    }

	@Override
	public PointAccountsBean increasePoints(PointAccountsBean account, int refundAmount, LocalDateTime refundExpireAt) {
		if (account == null) {
			throw new IllegalArgumentException("帳戶不得為 null");
		}

		if (refundAmount < 0) {
			throw new IllegalArgumentException("返還點數必須是非負整數");
		}

		int currentBalance = account.getTotalBalance();
		account.setTotalBalance(currentBalance + refundAmount); // 加點

		// 效期延展邏輯（取兩者較晚者）
		LocalDateTime currentExpireAt = account.getExpiredAt();
		if (refundExpireAt != null) {
			if (currentExpireAt == null || refundExpireAt.isAfter(currentExpireAt)) {
				account.setExpiredAt(refundExpireAt);
			}
		}

		account.setUpdatedAt(LocalDateTime.now());

		return pointAccountsRepository.save(account);
	}

	@Override
	@Transactional
	public void handleMonthlyPointReset() {
	    LocalDate today = LocalDate.now(); // 例如 2025-08-01
	    LocalDateTime now = LocalDateTime.of(today, LocalTime.MIDNIGHT);
	    LocalDateTime endOfThisMonth = today.withDayOfMonth(today.lengthOfMonth()).atTime(23, 59, 59);

	    // 1. 取得所有住戶（此例限定社區 1）
	    List<Integer> allUnitIds = unitsRepository.findUnitIdsByCommunityId(1);

	    for (Integer unitId : allUnitIds) {
	        Units unit = unitsRepository.getReferenceById(unitId);
	        Community community = unit.getCommunity();
	        PointAccountsBean account = pointAccountsRepository.findByUnitId(unitId);

	        // ✅ 檢查是否過期，若過期 → 先記錄，再清除點數
	        if (account != null && account.getExpiredAt() != null && account.getExpiredAt().isBefore(now)) {
	            int expiredPoints = account.getTotalBalance(); // ⬅️ 先記下來

	            if (expiredPoints > 0) {
	                PointTransactionsBean expiredTxn = new PointTransactionsBean();
	                expiredTxn.setCommunity(community);
	                expiredTxn.setUnit(unit);
	                expiredTxn.setTransactionType("EXPIRED");
	                expiredTxn.setAmount(-expiredPoints); // ⬅️ 確保是正確的點數
	                expiredTxn.setTransactionDescription("點數已到期，自動歸零");
	                expiredTxn.setCreatedAt(now);
	                pointTransactionsService.create(expiredTxn);
	            }

	            // ✅ 清除點數
	            account.setTotalBalance(0);
	        }

	        // ✅ 新帳戶或增發
	        if (account == null) {
	            account = new PointAccountsBean();
	            account.setUnit(unit);
	            account.setTotalBalance(100);
	        } else {
	            account.setTotalBalance(account.getTotalBalance() + 100);
	        }

	        account.setExpiredAt(endOfThisMonth);
	        pointAccountsRepository.save(account);

	        // ✅ 增發記錄
	        PointTransactionsBean systemTxn = new PointTransactionsBean();
	        systemTxn.setCommunity(community);
	        systemTxn.setUnit(unit);
	        systemTxn.setTransactionType("SYSTEM");
	        systemTxn.setAmount(100);
	        systemTxn.setTransactionDescription("系統每月增發點數");
	        systemTxn.setCreatedAt(now.plusSeconds(10));
	        pointTransactionsService.create(systemTxn);
	    }

	    // ⛔ 已經改為逐戶清除，不再需要這一行全域清除
	    // pointAccountsRepository.clearExpiredAccounts(now); // ← 拿掉！
	}

	


}
