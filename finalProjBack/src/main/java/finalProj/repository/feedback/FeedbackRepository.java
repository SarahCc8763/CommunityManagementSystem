package finalProj.repository.feedback;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.feedback.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    public List<Feedback> findByUser_UsersId(Integer id);

    public List<Feedback> findByCommunity_CommunityId(Integer id);
}
