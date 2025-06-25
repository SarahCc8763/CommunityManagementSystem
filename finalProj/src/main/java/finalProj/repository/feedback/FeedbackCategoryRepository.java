package finalProj.repository.feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.feedback.FeedbackCategory;

@Repository
public interface FeedbackCategoryRepository extends JpaRepository<FeedbackCategory, Integer> {

}
