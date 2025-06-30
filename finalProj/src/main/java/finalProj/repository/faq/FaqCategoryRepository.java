package finalProj.repository.faq;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.faq.FaqCategory;

@Repository
public interface FaqCategoryRepository extends JpaRepository<FaqCategory, Integer> {
    public Optional<FaqCategory> findByName(String name);

    public void deleteByName(String name);

    // findByCommunity_CommunityIdAndName
    public Optional<FaqCategory> findByCommunity_CommunityIdAndName(Integer id, String name);

    public List<FaqCategory> findByCommunity_CommunityId(Integer id);

}
