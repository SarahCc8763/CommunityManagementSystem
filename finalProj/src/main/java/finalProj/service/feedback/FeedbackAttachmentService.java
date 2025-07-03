package finalProj.service.feedback;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.feedback.FeedbackAttachment;
import finalProj.repository.feedback.FeedbackAttachmentRepository;

@Service
@Transactional
public class FeedbackAttachmentService {

    @Autowired
    private FeedbackAttachmentRepository feedbackAttachmentRepository;

    public List<FeedbackAttachment> findAll() {
        return feedbackAttachmentRepository.findAll();
    }

    public Optional<FeedbackAttachment> findById(Integer id) {
        return feedbackAttachmentRepository.findById(id);
    }

    public FeedbackAttachment save(FeedbackAttachment entity) {
        return feedbackAttachmentRepository.save(entity);
    }

    public void deleteById(Integer id) {
        feedbackAttachmentRepository.deleteById(id);
    }
}
