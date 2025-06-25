package finalProj.service.feedback;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.feedback.FeedbackReply;
import finalProj.repository.feedback.FeedbackReplyRepository;

@Service
@Transactional
public class FeedbackReplyService {

    @Autowired
    private FeedbackReplyRepository feedbackReplyRepository;

    public List<FeedbackReply> findAll() {
        return feedbackReplyRepository.findAll();
    }

    public Optional<FeedbackReply> findById(Integer id) {
        return feedbackReplyRepository.findById(id);
    }

    public FeedbackReply save(FeedbackReply entity) {
        return feedbackReplyRepository.save(entity);
    }

    public void deleteById(Integer id) {
        feedbackReplyRepository.deleteById(id);
    }
}
