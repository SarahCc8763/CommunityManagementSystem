package finalProj.repository.feedback;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.feedback.FeedbackStatusHistory;

@Repository
public interface FeedbackStatusHistoryRepository extends JpaRepository<FeedbackStatusHistory, Integer> {
    public List<FeedbackStatusHistory> findByFeedback_Id(Integer feedbackId);
}
