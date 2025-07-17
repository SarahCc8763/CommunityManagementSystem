package finalProj.service.feedback;

import java.time.LocalDateTime;
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
import finalProj.service.users.UsersService;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class FeedbackService {

    private final UsersService usersService;
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

    FeedbackService(UsersService usersService) {
        this.usersService = usersService;
    }
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
        feedback.setStatus("待處理");

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
        feedbackStatusHistory.setNewStatus("待處理");
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
                || feedback.getCommunity() == null || feedback.getCommunity().getCommunityId() == null) {
            log.info("資料不完整");
            return null;
        }

        // 查找相關實體
        Optional<FeedbackCategory> optionalCategory = feedbackCategoryRepository
                .findById(feedback.getCategory().getId());
        Optional<Users> optionalUser = usersRepository.findById(feedback.getUser().getUsersId());
        Optional<Community> optionalCommunity = communityRepository.findById(feedback.getCommunity().getCommunityId());

        if (optionalCategory.isEmpty() || optionalUser.isEmpty() || optionalCommunity.isEmpty()) {
            log.info("找不到對應的類別、反映使用者、處理者或社區");
            return null;
        }

        Feedback existing = optionalFeedback.get();
        Users user = optionalUser.get();
        Community community = optionalCommunity.get();

        if (!user.equals(existing.getUser()) || !community.equals(existing.getCommunity())) {
            log.info("傳入的反映者或社區與原資料不符，修改失敗");
            return null;
        }

        // 儲存原狀態（給後續判斷歷史記錄）
        String originalStatus = existing.getStatus();

        // 更新基本欄位
        existing.setTitle(feedback.getTitle());
        existing.setDescription(feedback.getDescription());
        existing.setCategory(optionalCategory.get());
        existing.setUser(user);
        existing.setCommunity(community);
        existing.setLastUpdated(LocalDateTime.now());

        // 滿意度
        if (feedback.getUserRating() != null) {
            existing.setUserRating(feedback.getUserRating());
        }

        // 狀態更新與結案時間
        if (feedback.getStatus() != null && !feedback.getStatus().isBlank()) {
            if (!"已結案".equals(originalStatus) && "已結案".equals(feedback.getStatus())) {
                existing.setResolvedAt(LocalDateTime.now());
            }
            existing.setStatus(feedback.getStatus());
        }

        // ✅ 承辦人處理（不允許清空）
        if (feedback.getHandler() != null && feedback.getHandler().getUsersId() != null) {
            Optional<Users> optionalHandler = usersRepository.findById(feedback.getHandler().getUsersId());
            if (optionalHandler.isEmpty()) {
                log.warn("承辦人 ID 無效，找不到使用者");
                return null;
            }
            existing.setHandler(optionalHandler.get());
        } else {
            log.info("未更新承辦人，保留原有設定");
        }

        // 處理附件（清除 + 加入新）
        if (existing.getAttachments() != null) {
            existing.getAttachments().clear();
            log.debug("已清空原有附件");
        }

        if (feedback.getAttachments() != null && !feedback.getAttachments().isEmpty()) {
            for (FeedbackAttachment newAttachment : feedback.getAttachments()) {
                newAttachment.setFeedback(existing);

                if (newAttachment.getFileDataBase64() != null) {
                    newAttachment.setFileSize(newAttachment.getFileDataBase64().length() * 3 / 4);
                } else if (newAttachment.getAttachment() != null) {
                    newAttachment.setFileSize(newAttachment.getAttachment().length * 3 / 4);
                }

                if (newAttachment.getFileName().length() > 50) {
                    newAttachment.setFileName(newAttachment.getFileName().substring(0, 50));
                }

                existing.getAttachments().add(newAttachment);
                log.debug("加入新附件：{}", newAttachment.getFileName());
            }
        }

        // 若狀態變更則建立歷史紀錄
        if (feedback.getStatus() != null && !feedback.getStatus().equals(originalStatus)) {
            FeedbackStatusHistory history = new FeedbackStatusHistory();
            history.setFeedback(existing);
            history.setOldStatus(originalStatus);
            history.setNewStatus(feedback.getStatus());
            history.setChangedAt(LocalDateTime.now());
            history.setChangedBy(user);
            feedbackStatusHistoryRepository.save(history);
            log.info("狀態已變更，新增歷史紀錄：{} -> {}", originalStatus, feedback.getStatus());
        }

        return feedbackRepository.save(existing);
    }

    public Feedback updateStatus(Feedback feedback) {
        Optional<Feedback> optionalFeedback = feedbackRepository.findById(feedback.getId());
        if (optionalFeedback.isEmpty()) {
            log.info("找不到對應的Feedback");
            return null;
        }

        Feedback existing = optionalFeedback.get();

        // 儲存原狀態（給後續判斷歷史記錄）
        String originalStatus = existing.getStatus();

        // 滿意度
        // if (feedback.getUserRating() != null) {
        // existing.setUserRating(feedback.getUserRating());
        // }

        // 狀態更新與結案時間
        if (feedback.getStatus() != null && !feedback.getStatus().isBlank()) {
            if (!"已結案".equals(originalStatus) && "已結案".equals(feedback.getStatus())) {
                existing.setResolvedAt(LocalDateTime.now());
            }
            existing.setStatus(feedback.getStatus());
        }

        // ✅ 承辦人處理（不允許清空）
        if (feedback.getHandler() != null && feedback.getHandler().getUsersId() != null) {
            Optional<Users> optionalHandler = usersRepository.findById(feedback.getHandler().getUsersId());
            if (optionalHandler.isEmpty()) {
                log.warn("承辦人 ID 無效，找不到使用者");
                return null;
            }
            existing.setHandler(optionalHandler.get());
        } else {
            log.info("未更新承辦人，保留原有設定");
        }

        // 若狀態變更則建立歷史紀錄
        if (feedback.getStatus() != null && !feedback.getStatus().equals(originalStatus)) {
            FeedbackStatusHistory history = new FeedbackStatusHistory();
            history.setFeedback(existing);
            history.setOldStatus(originalStatus);
            history.setNewStatus(feedback.getStatus());
            history.setChangedAt(LocalDateTime.now());
            history.setChangedBy(usersService.findById(feedback.getHandler().getUsersId()));
            feedbackStatusHistoryRepository.save(history);
            log.info("狀態已變更，新增歷史紀錄：{} -> {}", originalStatus, feedback.getStatus());
        }

        return feedbackRepository.save(existing);
    }

    public Feedback rating(Feedback feedback) {
        Optional<Feedback> optionalFeedback = feedbackRepository.findById(feedback.getId());
        if (optionalFeedback.isEmpty()) {
            log.info("找不到對應的Feedback");
            return null;
        }

        Feedback existing = optionalFeedback.get();

        // 滿意度
        if (feedback.getUserRating() != null) {
            existing.setUserRating(feedback.getUserRating());
            log.info("已更新意見ID {} 使用者滿意度為 {} 星", feedback.getId(), feedback.getUserRating());
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

    public List<Feedback> findAll(Integer communityId) {
        return feedbackRepository.findByCommunity_CommunityId(communityId);
    }

    public Long count() {
        return feedbackRepository.count();
    }

    public List<Feedback> findByUser_UsersId(Integer id) {
        return feedbackRepository.findByUser_UsersId(id);

    }

}
