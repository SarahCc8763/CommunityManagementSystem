package finalProj.repository.users;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import finalProj.domain.users.Units;

@Repository
public interface UnitsRepository extends JpaRepository<Units, Integer> {

    Units findByUnitsId(Integer unitsId);

    Optional<Units> findByUnitAndFloor(String unit, String floor);

    public List<Units> findByCommunity_CommunityId(Integer communityId);

     @Query("SELECT u.unitsId FROM Units u WHERE u.community.id = :communityId")
    List<Integer> findUnitIdsByCommunityId(@Param("communityId") Integer communityId);

    // 查詢點數可轉入unit方法
    List<Units> findByCommunityCommunityIdAndUnitsIdNot(Integer communityId, Integer excludeUnitId);
}
