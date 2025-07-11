package finalProj.repository.parking;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.parking.LotteryApply;

public interface LotteryApplyRepository extends JpaRepository<LotteryApply, Integer> {

	boolean existsByUsers_UsersIdAndLotteryEvents_BulletinId(Integer usersId, Integer bulletinId);

	// 查詢某抽籤活動的所有申請者
	List<LotteryApply> findByLotteryEvents_BulletinId(Integer bulletinId);

	// 查詢某用戶的申請紀錄（可選）
	List<LotteryApply> findByUsers_UsersId(Integer usersId);

	// 查詢某活動中某位使用者是否已申請（可作防呆驗證）
	boolean existsByLotteryEvents_BulletinIdAndUsers_UsersId(Integer eventId, Integer usersId);

}
