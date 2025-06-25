package finalProj.service.bulletin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.bulletin.BulletinCategory;

import finalProj.repository.bulletin.BulletinCategoryRepository;
import finalProj.repository.community.CommunityRepository;

@Service
@Transactional
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
        return bulletinCategoryRepository.save(entity);
    }

    // 不能用，因為name是主鍵不能修改，除非要新增Id當主鍵
    // public BulletinCategory modify(BulletinCategory entity) {
    // Optional<BulletinCategory> optional =
    // bulletinCategoryRepository.findByName(entity.getName());
    // if (optional.isPresent()) {
    // return bulletinCategoryRepository.save(modified);
    // }
    // return null;
    // }

    public String deleteByName(String name) {

        if (!bulletinCategoryRepository.existsByName(name)) {
            // System.out.println("找不到該分類，刪除失敗");
            return "找不到該分類，刪除失敗";
        }
        if (!bulletinCategoryRepository.findByName(name).get().getBulletins().isEmpty()) {
            // System.out.println("有外鍵約束，刪除失敗");
            return "有外鍵約束，刪除失敗";
        }
        bulletinCategoryRepository.deleteByName(name);
        return "刪除成功";

    }

}
