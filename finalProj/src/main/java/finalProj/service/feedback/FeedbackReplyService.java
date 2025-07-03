package finalProj.service.feedback;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.feedback.Feedback;
import finalProj.domain.feedback.FeedbackReply;
import finalProj.domain.feedback.FeedbackStatusHistory;
import finalProj.domain.users.Users;
import finalProj.repository.feedback.FeedbackReplyRepository;
import finalProj.repository.feedback.FeedbackRepository;
import finalProj.repository.feedback.FeedbackStatusHistoryRepository;
import finalProj.repository.users.UsersRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class FeedbackReplyService {

    @Autowired
    private FeedbackReplyRepository feedbackReplyRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;

    // @Autowired
    // private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackStatusHistoryRepository feedbackStatusHistoryRepository;

    public List<FeedbackReply> findAll() {
        return feedbackReplyRepository.findAll();
    }

    public Optional<FeedbackReply> findById(Integer id) {
        return feedbackReplyRepository.findById(id);
    }

    public FeedbackReply insert(FeedbackReply entity) {
        // 設定回覆時間
        entity.setRepliedAt(LocalDateTime.now());

        // 儲存回覆
        FeedbackReply savedReply = feedbackReplyRepository.save(entity);

        // 檢查是否需要建立狀態歷史記錄
        String newStatus = entity.getNewFeedBackStatus();
        String oldStatus = entity.getPreFeedBackStatus();

        if (newStatus != null && !newStatus.equals(oldStatus)) {
            // 查詢 Feedback（這邊應該是用 feedbackId 而不是 userId）
            Integer feedbackId = entity.getFeedback().getId();
            Optional<Feedback> optFeedback = feedbackRepository.findById(feedbackId);

            if (optFeedback.isPresent()) {
                Feedback feedback = optFeedback.get();

                // 更新 feedback 的狀態
                feedback.setStatus(newStatus);
                feedback.setLastUpdated(LocalDateTime.now());
                feedbackRepository.save(feedback); // 寫入資料庫

                // 建立狀態歷史紀錄
                FeedbackStatusHistory statusHistory = new FeedbackStatusHistory();
                statusHistory.setFeedback(feedback);
                statusHistory.setChangedBy(entity.getUser());
                statusHistory.setOldStatus(oldStatus);
                statusHistory.setNewStatus(newStatus);
                statusHistory.setChangedAt(LocalDateTime.now());

                feedbackStatusHistoryRepository.save(statusHistory);
            } else {
                log.warn("找不到 Feedback，ID: {}", feedbackId);
            }
        } else {
            log.info("狀態未變更，跳過建立歷史紀錄");
        }

        return savedReply;
    }

    public FeedbackReply modify(FeedbackReply entity) {
        Optional<Users> optional = usersRepository.findById(entity.getUser().getUsersId());
        if (optional.isEmpty()) {
            log.error("使用者資料有誤");
            return null;
        }
        Users user = optional.get();
        entity.setUser(user);
        entity.setRepliedAt(LocalDateTime.now());
        return feedbackReplyRepository.save(entity);

    }

    public void deleteById(Integer id) {
        feedbackReplyRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return feedbackReplyRepository.existsById(id);
    }
}
