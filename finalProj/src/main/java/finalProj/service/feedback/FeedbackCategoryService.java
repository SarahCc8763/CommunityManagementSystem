package finalProj.service.feedback;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.feedback.FeedbackCategory;
import finalProj.repository.feedback.FeedbackCategoryRepository;

@Service
@Transactional
public class FeedbackCategoryService {

    @Autowired
    private FeedbackCategoryRepository feedbackCategoryRepository;

    public List<FeedbackCategory> findAll() {
        return feedbackCategoryRepository.findAll();
    }

    public Optional<FeedbackCategory> findById(Integer id) {
        return feedbackCategoryRepository.findById(id);
    }

    public FeedbackCategory save(FeedbackCategory entity) {
        return feedbackCategoryRepository.save(entity);
    }

    public void deleteById(Integer id) {
        feedbackCategoryRepository.deleteById(id);
    }
}
