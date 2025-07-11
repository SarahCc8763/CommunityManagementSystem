package finalProj.repository.bulletin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.bulletin.BulletinCategory;

@Repository
public interface BulletinCategoryRepository extends JpaRepository<BulletinCategory, Integer> {
    public Optional<BulletinCategory> findByName(String name);

    public void deleteByName(String name);

    public boolean existsByName(String name);

    public List<BulletinCategory> findByCommunity_CommunityId(Integer id);

    public Optional<BulletinCategory> findByCommunity_CommunityIdAndName(Integer id, String name);
}
