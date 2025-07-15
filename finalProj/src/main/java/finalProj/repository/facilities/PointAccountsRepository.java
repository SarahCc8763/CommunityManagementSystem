package finalProj.repository.facilities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import finalProj.domain.facilities.PointAccountsBean;

@Repository
public interface PointAccountsRepository extends JpaRepository<PointAccountsBean, Integer> {
	
	 Optional<PointAccountsBean> findByUnit_UnitsId(Integer unitId);
	 
	 
	 @Modifying
	 @Query("UPDATE PointAccountsBean a SET a.totalBalance = 0 WHERE a.expiredAt < :now")
	 void clearExpiredAccounts(@Param("now") LocalDateTime now);

	 @Query("SELECT u.unitsId FROM Units u")
	 List<Integer> findAllUnitIds();

	 @Query("SELECT a FROM PointAccountsBean a WHERE a.unit.unitsId = :unitId")
	 PointAccountsBean findByUnitId(@Param("unitId") Integer unitId);
}
