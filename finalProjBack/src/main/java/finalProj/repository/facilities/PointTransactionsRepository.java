package finalProj.repository.facilities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.facilities.PointTransactionsBean;

@Repository
public interface PointTransactionsRepository extends JpaRepository<PointTransactionsBean, Integer> {

	// 查特定住戶的所有交易紀錄
	List<PointTransactionsBean> findByUnit_UnitsId(Integer unitId);
	
}
