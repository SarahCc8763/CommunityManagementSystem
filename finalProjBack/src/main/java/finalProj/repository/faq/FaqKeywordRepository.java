package finalProj.repository.faq;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.faq.FaqKeyword;

@Repository
public interface FaqKeywordRepository extends JpaRepository<FaqKeyword, Integer> {

    public Boolean existsByWord(String word);

    public Optional<FaqKeyword> findByWord(String word);
}
