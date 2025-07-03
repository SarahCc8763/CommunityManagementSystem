package finalProj.repository.feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.feedback.FeedbackAttachment;

@Repository
public interface FeedbackAttachmentRepository extends JpaRepository<FeedbackAttachment, Integer> {

}
