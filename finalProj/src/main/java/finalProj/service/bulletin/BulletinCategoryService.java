package finalProj.service.bulletin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.bulletin.BulletinCategory;

import finalProj.repository.bulletin.BulletinCategoryRepository;
import finalProj.repository.community.CommunityRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class BulletinCategoryService {

    @Autowired
    private BulletinCategoryRepository bulletinCategoryRepository;

    @Autowired
    CommunityRepository communityRepository;

    public List<BulletinCategory> findAll() {
        return bulletinCategoryRepository.findAll();
    }

    public Optional<BulletinCategory> findByName(String name) {
        return bulletinCategoryRepository.findByName(name);
    }

    public BulletinCategory save(BulletinCategory entity) {
        if (entity.getName() == null) {
            log.warn("分類新增失敗：分類名稱不能為空");
            return null;

        }
        return bulletinCategoryRepository.save(entity);
    }

    // 不能用，因為name是主鍵不能修改，除非要新增Id當主鍵
    // ->所以如果新增的時候打錯，只能刪除重新新增，既有的分類且已經被外鍵依賴就沒救了
    // public BulletinCategory modify(BulletinCategory entity) {
    // Optional<BulletinCategory> optional =
    // bulletinCategoryRepository.findByName(entity.getName());
    // if (optional.isPresent()) {
    // return bulletinCategoryRepository.save(modified);
    // }
    // return null;
    // }

    // 刪除分類
    public String delete(Integer id) {

        log.info("準備刪除分類：{}", id);

        if (!bulletinCategoryRepository.existsById(id)) {
            log.warn("分類刪除失敗：找不到分類 {}", id);
            return "找不到該分類，刪除失敗";
        }

        var categoryOpt = bulletinCategoryRepository.findById(id);
        if (categoryOpt.isPresent() && !categoryOpt.get().getBulletins().isEmpty()) {
            log.warn("分類刪除失敗：存在關聯公告，無法刪除");
            return "有外鍵約束，刪除失敗";
        }

        bulletinCategoryRepository.deleteById(id);
        log.info("分類刪除成功：{}", id);
        return "刪除成功";
    }

}
