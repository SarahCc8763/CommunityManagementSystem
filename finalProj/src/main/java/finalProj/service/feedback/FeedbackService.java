package finalProj.service.feedback;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.community.Community;
import finalProj.domain.feedback.Feedback;
import finalProj.domain.feedback.FeedbackCategory;
import finalProj.domain.users.Users;
import finalProj.repository.community.CommunityRepository;
import finalProj.repository.feedback.FeedbackCategoryRepository;
import finalProj.repository.feedback.FeedbackRepository;
import finalProj.repository.users.UsersRepository;

@Service
@Transactional
public class FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private FeedbackCategoryRepository feedbackCategoryRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CommunityRepository communityRepository;

    //
    // 新增
    //

    public Feedback insert(Feedback feedback) {
        // 確認資料完整性
        if (feedback.getCategory() == null || feedback.getCategory().getId() == null
                || feedback.getUser() == null || feedback.getUser().getUsersId() == null
                || feedback.getHandler() == null || feedback.getHandler().getUsersId() == null
                || feedback.getCommunity() == null || feedback.getCommunity().getCommunityId() == null) {
            return null;
        }

        // 從傳入的類別id尋找完整類別、反映人、處理者、社區物件
        Optional<FeedbackCategory> optionalCategory = feedbackCategoryRepository
                .findById(feedback.getCategory().getId());
        Optional<Users> optionalUser = usersRepository.findById(feedback.getUser().getUsersId());
        Optional<Users> optionalHandler = usersRepository.findById(feedback.getHandler().getUsersId());
        Optional<Community> optionalCommunity = communityRepository.findById(feedback.getCommunity().getCommunityId());

        // 任何一個沒找到就傳null
        if (!optionalCategory.isPresent() || !optionalUser.isPresent() || !optionalHandler.isPresent()
                || !optionalCommunity.isPresent()) {
            return null;
        }
        // 如果找到就放到要新增的feedback物件的對應屬性裡
        feedback.setCategory(optionalCategory.get());
        feedback.setUser(optionalUser.get());
        feedback.setHandler(optionalHandler.get());
        feedback.setCommunity(optionalCommunity.get());

        return feedbackRepository.save(feedback);

    }

    public Feedback modify(Feedback feedback) {
        // 確認資料完整性
        if (feedback.getCategory() == null || feedback.getCategory().getId() == null
                || feedback.getUser() == null || feedback.getUser().getUsersId() == null
                || feedback.getHandler() == null || feedback.getHandler().getUsersId() == null
                || feedback.getCommunity() == null || feedback.getCommunity().getCommunityId() == null) {
            return null;
        }

        // 從傳入的類別id尋找完整類別、反映人、處理者、社區物件
        Optional<FeedbackCategory> optionalCategory = feedbackCategoryRepository
                .findById(feedback.getCategory().getId());
        Optional<Users> optionalUser = usersRepository.findById(feedback.getUser().getUsersId());
        Optional<Users> optionalHandler = usersRepository.findById(feedback.getHandler().getUsersId());
        Optional<Community> optionalCommunity = communityRepository.findById(feedback.getCommunity().getCommunityId());

        // 任何一個沒找到就傳null
        if (!optionalCategory.isPresent() || !optionalUser.isPresent() || !optionalHandler.isPresent()
                || !optionalCommunity.isPresent()) {
            return null;
        }

        // 如果找到就放到要新增的feedback物件的對應屬性裡
        feedback.setCategory(optionalCategory.get());
        feedback.setUser(optionalUser.get());
        feedback.setHandler(optionalHandler.get());
        feedback.setCommunity(optionalCommunity.get());
        feedback.setLastUpdated(LocalDateTime.now());
        if (feedback.getUserRating() == null) {
            feedbackRepository.findById(feedback.getId())
                    .ifPresent(original -> feedback.setUserRating(original.getUserRating()));
        }
        if (feedback.getStatus() == null) {
            feedbackRepository.findById(feedback.getId())
                    .ifPresent(original -> feedback.setStatus(original.getStatus()));
        }

        return feedbackRepository.save(feedback);
    }

    public boolean deleteFeedback(Integer id) {

        return false;
    }

    public Feedback findById(Integer id) {
        Optional<Feedback> optional = feedbackRepository.findById(id);
        return optional.orElse(null);
    }

    public boolean existById(Integer id) {
        return feedbackRepository.existsById(id);
    }

    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }

    public Long count() {
        return feedbackRepository.count();
    }

}
