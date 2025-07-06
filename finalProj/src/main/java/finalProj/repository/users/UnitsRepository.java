package finalProj.repository.users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.users.Units;

@Repository
public interface UnitsRepository extends JpaRepository<Units, Integer> {

    Units findByUnitsId(Integer unitsId);

    public List<Units> findByCommunity_CommunityId(Integer communityId);
}
