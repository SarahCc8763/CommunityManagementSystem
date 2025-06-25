package finalProj.service.feedback;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.feedback.Feedback;
import finalProj.repository.feedback.FeedbackRepository;

@Service
@Transactional
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }

    public Optional<Feedback> findById(Integer id) {
        return feedbackRepository.findById(id);
    }

    public Feedback save(Feedback entity) {
        return feedbackRepository.save(entity);
    }

    public void deleteById(Integer id) {
        feedbackRepository.deleteById(id);
    }
}
