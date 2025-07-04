package finalProj.service.feedback;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.feedback.FeedbackStatusHistory;
import finalProj.repository.feedback.FeedbackStatusHistoryRepository;

@Service
@Transactional
public class FeedbackStatusHistoryService {

    @Autowired
    private FeedbackStatusHistoryRepository feedbackStatusHistoryRepository;

    public List<FeedbackStatusHistory> findAll() {
        return feedbackStatusHistoryRepository.findAll();
    }

    public Optional<FeedbackStatusHistory> findById(Integer id) {
        return feedbackStatusHistoryRepository.findById(id);
    }

    public FeedbackStatusHistory save(FeedbackStatusHistory entity) {
        return feedbackStatusHistoryRepository.save(entity);
    }

    public void deleteById(Integer id) {
        feedbackStatusHistoryRepository.deleteById(id);
    }
}
