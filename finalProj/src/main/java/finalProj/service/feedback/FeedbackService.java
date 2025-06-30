package finalProj.service.feedback;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.community.Community;
import finalProj.domain.feedback.Feedback;
import finalProj.domain.feedback.FeedbackAttachment;
import finalProj.domain.feedback.FeedbackCategory;
import finalProj.domain.feedback.FeedbackStatusHistory;
import finalProj.domain.users.Users;
import finalProj.repository.community.CommunityRepository;
import finalProj.repository.feedback.FeedbackAttachmentRepository;
import finalProj.repository.feedback.FeedbackCategoryRepository;
import finalProj.repository.feedback.FeedbackRepository;
import finalProj.repository.feedback.FeedbackStatusHistoryRepository;
import finalProj.repository.users.UsersRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private FeedbackCategoryRepository feedbackCategoryRepository;
    @Autowired
    private FeedbackAttachmentRepository feedbackAttachmentRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private FeedbackStatusHistoryRepository feedbackStatusHistoryRepository;
    //
    // 新增
    //

    public Feedback insert(Feedback feedback) {
        // 確認資料完整性
        if (feedback.getCategory() == null || feedback.getCategory().getId() == null
                || feedback.getUser() == null || feedback.getUser().getUsersId() == null
                || feedback.getCommunity() == null || feedback.getCommunity().getCommunityId() == null) {
            return null;
        }

        // 從傳入的類別id尋找完整類別、反映人、社區物件
        Optional<FeedbackCategory> optionalCategory = feedbackCategoryRepository
                .findById(feedback.getCategory().getId());
        Optional<Users> optionalUser = usersRepository.findById(feedback.getUser().getUsersId());

        Optional<Community> optionalCommunity = communityRepository.findById(feedback.getCommunity().getCommunityId());

        // 任何一個沒找到就傳null
        if (!optionalCategory.isPresent() || !optionalUser.isPresent() || !optionalCommunity.isPresent()) {
            log.info("找不到對應的類別、反映使用者、處理者或社區");
            return null;
        }

        if (feedback.getHandler() != null && feedback.getHandler().getUsersId() != null) {
            Optional<Users> optionalHandler = usersRepository.findById(feedback.getHandler().getUsersId());
            if (optionalHandler.isEmpty()) {
                log.info("找不到對應的處理者");
                return null;
            }
            feedback.setHandler(optionalHandler.get());
        }

        // 如果找到就放到要新增的feedback物件的對應屬性裡
        feedback.setCategory(optionalCategory.get());
        feedback.setUser(optionalUser.get());
        feedback.setCommunity(optionalCommunity.get());

        Feedback savedFeedback = feedbackRepository.save(feedback);

        // 儲存附件
        List<FeedbackAttachment> attachments = feedback.getAttachments();
        if (attachments != null) {
            attachments.forEach(a -> {
                a.setFeedback(savedFeedback);
                feedbackAttachmentRepository.save(a);
                log.info("附件儲存成功：{}", a.getFileName());
            });
        }
        log.info("Feedback儲存成功：Id為 {}", savedFeedback.getId());

        // 新增狀態歷史紀錄
        FeedbackStatusHistory feedbackStatusHistory = new FeedbackStatusHistory();
        feedbackStatusHistory.setFeedback(savedFeedback);
        feedbackStatusHistory.setOldStatus(null);
        feedbackStatusHistory.setNewStatus(savedFeedback.getStatus());
        feedbackStatusHistory.setChangedAt(LocalDateTime.now());
        feedbackStatusHistory.setChangedBy(savedFeedback.getUser());
        feedbackStatusHistoryRepository.save(feedbackStatusHistory);
        return savedFeedback;

    }

    public Feedback modify(Feedback feedback) {
        Optional<Feedback> optionalFeedback = feedbackRepository.findById(feedback.getId());
        if (optionalFeedback.isEmpty()) {
            log.info("找不到對應的Feedback");
            return null;
        }

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
        Users user = (optionalUser.get());
        Community community = optionalCommunity.get();

        Feedback existing = optionalFeedback.get();
        if (!user.equals(existing.getHandler()) || !community.equals(existing.getCommunity())) {
            log.info("傳入的反映者或社區與原資料不符，修改失敗");
            return null;
        }

        // 如果都找到就放到要新增的feedback物件的對應屬性裡
        existing.setCategory(optionalCategory.get());
        existing.setUser(user);
        existing.setHandler(optionalHandler.get());
        existing.setCommunity(community);
        existing.setLastUpdated(LocalDateTime.now());
        if (feedback.getUserRating() != null) {
            existing.setUserRating(feedback.getUserRating());
        }

        // 移除舊附件
        if (existing.getAttachments() != null && !existing.getAttachments().isEmpty()) {
            feedbackAttachmentRepository.deleteAll(existing.getAttachments());
            existing.getAttachments().clear();
            log.debug("已刪除舊有附件");
        }

        // 加入新附件（若有）
        if (feedback.getAttachments() != null && !feedback.getAttachments().isEmpty()) {
            List<FeedbackAttachment> newAttachments = new ArrayList<>();
            for (FeedbackAttachment newAttachment : feedback.getAttachments()) {
                newAttachment.setFeedback(existing);
                newAttachments.add(newAttachment);
                log.debug("加入新附件：{}", newAttachment.getFileName());
            }
            existing.setAttachments(newAttachments);
        }

        // 修改狀態歷史紀錄
        if (feedback.getStatus() != null && !feedback.getStatus().equals(existing.getStatus())) {
            log.info("狀態有變更，修改意見狀態，並新增狀態歷史紀錄");
            FeedbackStatusHistory feedbackStatusHistory = new FeedbackStatusHistory();
            feedbackStatusHistory.setFeedback(existing);
            feedbackStatusHistory.setOldStatus(existing.getStatus());
            feedbackStatusHistory.setNewStatus(feedback.getStatus());
            existing.setStatus(feedback.getStatus());
            feedbackStatusHistory.setChangedAt(LocalDateTime.now());
            feedbackStatusHistory.setChangedBy(user);
            feedbackStatusHistoryRepository.save(feedbackStatusHistory);
        }

        return feedbackRepository.save(existing);

    }

    public boolean deleteFeedback(Integer id) {
        if (feedbackRepository.existsById(id)) {
            feedbackRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Feedback findById(Integer id) {
        Optional<Feedback> optional = feedbackRepository.findById(id);
        return optional.orElse(null);
    }

    public boolean existsById(Integer id) {
        return feedbackRepository.existsById(id);
    }

    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }

    public Long count() {
        return feedbackRepository.count();
    }

}
