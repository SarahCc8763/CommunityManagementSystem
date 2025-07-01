package finalProj.repository.feedback;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.feedback.FeedbackCategory;

@Repository
public interface FeedbackCategoryRepository extends JpaRepository<FeedbackCategory, Integer> {

    public List<FeedbackCategory> findByCommunity_CommunityId(int communityId);
}
