package finalProj.service.faq;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.faq.FaqCategory;
import finalProj.repository.faq.FaqCategoryRepository;
import finalProj.service.community.CommunityService;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class FaqCategoryService {

    @Autowired
    private FaqCategoryRepository faqCategoryRepository;
    @Autowired
    private CommunityService CommunityService;

    public List<FaqCategory> findAll() {
        return faqCategoryRepository.findAll();
    }

    public Optional<FaqCategory> findByName(String name) {
        return faqCategoryRepository.findByName(name);
    }

    public Optional<FaqCategory> findById(Integer id) {
        return faqCategoryRepository.findById(id);
    }

    public FaqCategory save(FaqCategory entity) {
        if (entity.getCommunity() == null || entity.getCommunity().getCommunityId() == null) {
            return null;
        }
        if (!findByCommunity_CommunityIdAndName(entity.getCommunity().getCommunityId(), entity.getName()).isEmpty()) {
            log.error("FAQ 分類已經存在");
            return null;
        }
        entity.setCommunity(CommunityService.findById(entity.getCommunity().getCommunityId()));
        return faqCategoryRepository.save(entity);
    }

    public void deleteByName(String name) {
        faqCategoryRepository.deleteByName(name);
    }

    public List<FaqCategory> findByCommunity_CommunityIdAndName(Integer id, String name) {
        return faqCategoryRepository.findByCommunity_CommunityIdAndName(id, name);
    }

    public List<FaqCategory> findByCommunity_CommunityId(Integer id) {
        return faqCategoryRepository.findByCommunity_CommunityId(id);
    }

    public Boolean delete(FaqCategory entity) {
        try {
            faqCategoryRepository.delete(entity);
            log.info("FAQ 分類已刪除");
            return true;

        } catch (Exception e) {
            log.error("FAQ 分類刪除失敗");
            return false;
        }

    }

}
