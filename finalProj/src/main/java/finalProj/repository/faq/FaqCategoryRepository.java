package finalProj.repository.faq;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.faq.FaqCategory;

@Repository
public interface FaqCategoryRepository extends JpaRepository<FaqCategory, String> {
    public Optional<FaqCategory> findByName(String name);

    public void deleteByName(String name);
}
