package finalProj.repository.bulletin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import finalProj.domain.bulletin.Bulletin;

@Repository
public interface BulletinRepository extends JpaRepository<Bulletin, Integer> {

        public boolean existsById(Integer id);

        @Query("SELECT DISTINCT b FROM Bulletin b " +
                        "LEFT JOIN b.category bc " +
                        "WHERE (:category IS NULL OR bc.name = :category) " +
                        "AND (:title IS NULL OR b.title like %:title%) ")
        public List<Bulletin> findByCategoryAndTitle(@Param("category") String category,
                        @Param("title") String title);

        public List<Bulletin> findByCommunity_communityId(Integer communityId);
}
