package finalProj.repository.parking;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import finalProj.domain.parking.LotteryApply;
import finalProj.dto.parking.WinnerDTO;

public interface LotteryApplyRepository extends JpaRepository<LotteryApply, Integer> {

	boolean existsByUsers_UsersIdAndLotteryEvents_BulletinId(Integer usersId, Integer bulletinId);

	// 查詢某抽籤活動的所有申請者
	List<LotteryApply> findByLotteryEvents_BulletinId(Integer bulletinId);

	// 查詢某用戶的申請紀錄（可選）
	List<LotteryApply> findByUsers_UsersId(Integer usersId);

	// 查詢某活動中某位使用者是否已申請（可作防呆驗證）
	boolean existsByLotteryEvents_BulletinIdAndUsers_UsersId(Integer eventId, Integer usersId);

	// 查詢某抽籤活動的得獎者
	@Query("""
			   SELECT new finalProj.dto.parking.WinnerDTO(
			       a.id, u.usersId, u.name, s.slotNumber, pt.type
			   )
			   FROM LotteryApply a
			   JOIN a.users u
			   JOIN a.lotteryEventSpace es
			   JOIN es.parkingSlot s
			   JOIN s.parkingType pt
			   WHERE a.lotteryEvents.bulletinId = :eventId
			     AND a.lotteryEventSpace IS NOT NULL
			""")
	List<WinnerDTO> findWinnersByEventId(@Param("eventId") Integer eventId);

}
