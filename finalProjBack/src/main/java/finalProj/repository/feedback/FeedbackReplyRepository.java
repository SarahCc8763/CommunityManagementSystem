package finalProj.repository.feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.feedback.FeedbackReply;

@Repository
public interface FeedbackReplyRepository extends JpaRepository<FeedbackReply, Integer> {

}
