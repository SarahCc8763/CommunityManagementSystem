package finalProj.repository.faq;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import finalProj.domain.faq.Faq;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Integer> {

        @Query("SELECT DISTINCT f FROM Faq f " +
                        "LEFT JOIN f.category fc " +
                        "LEFT JOIN f.keywordLinks fk " +
                        "LEFT JOIN fk.keyword k " +
                        "WHERE (:category IS NULL OR fc.name = :category) " +
                        "AND (:keywords IS NULL OR k.word IN :keywords)")
        List<Faq> findByCategoryAndKeywords(@Param("category") String category,
                        @Param("keywords") List<String> keywords);

}
