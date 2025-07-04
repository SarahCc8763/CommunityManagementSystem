package finalProj.service.feedback;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.feedback.FeedbackCategory;
import finalProj.repository.feedback.FeedbackCategoryRepository;
import finalProj.service.community.CommunityService;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class FeedbackCategoryService {

    @Autowired
    private FeedbackCategoryRepository feedbackCategoryRepository;

    @Autowired
    private CommunityService communityService;

    public List<FeedbackCategory> findAll() {
        return feedbackCategoryRepository.findAll();
    }

    public Optional<FeedbackCategory> findById(Integer id) {
        return feedbackCategoryRepository.findById(id);
    }

    public FeedbackCategory save(FeedbackCategory entity) {
        if (entity.getCommunity() != null && entity.getCommunity().getCommunityId() != null) {
            entity.setCommunity(communityService.findById(entity.getCommunity().getCommunityId()));
        }

        if (entity.getName() == null) {
            log.error("未提供分類名稱");
            return null;
        }
        return feedbackCategoryRepository.save(entity);
    }

    public String deleteById(Integer id) {
        Optional<FeedbackCategory> categoryOpt = feedbackCategoryRepository.findById(id);
        if (categoryOpt.isEmpty()) {
            log.error("分類刪除失敗：該分類不存在");
            return "該分類不存在";
        }
        FeedbackCategory entity = categoryOpt.get();
        String name = entity.getName();
        log.info("準備刪除分類：{}", name);

        if (categoryOpt.get().getFeedbacks().isEmpty()) {
            log.warn("分類刪除失敗：{} 存在關聯公告，無法刪除", name);
            return "該分類內尚有資料（有外鍵約束），刪除失敗";
        }

        feedbackCategoryRepository.deleteById(id);
        log.info("分類刪除成功：{}", name);
        return "刪除成功";

    }

    public List<FeedbackCategory> findByCommunityId(Integer communityId) {
        return feedbackCategoryRepository.findByCommunity_CommunityId(communityId);
    }
}
