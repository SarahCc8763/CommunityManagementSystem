package finalProj.repository.faq;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.embed.FaqFaqKeywordId;
import finalProj.domain.faq.FaqFaqKeyword;

@Repository
public interface FaqFaqKeywordRepository extends JpaRepository<FaqFaqKeyword, FaqFaqKeywordId> {

}
