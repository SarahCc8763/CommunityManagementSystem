package finalProj.repository.bulletin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.bulletin.BulletinCategory;

@Repository
public interface BulletinCategoryRepository extends JpaRepository<BulletinCategory, String> {
    public Optional<BulletinCategory> findByName(String name);

    public void deleteByName(String name);
}
