package finalProj.service.bulletin;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.bulletin.BulletinComment;
import finalProj.repository.bulletin.BulletinCommentRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class BulletinCommentService {

    // @Autowired
    // private UsersService usersService;

    // @Autowired
    // private BulletinService bulletinService;

    @Autowired
    private BulletinCommentRepository bulletinCommentRepository;

    public List<BulletinComment> findAll() {
        return bulletinCommentRepository.findAll();
    }

    public Optional<BulletinComment> findById(Integer id) {

        return bulletinCommentRepository.findById(id);
    }

    public BulletinComment save(BulletinComment entity) {

        if (entity.getParentComment() != null && entity.getParentComment().getId() != null) {
            BulletinComment parent = bulletinCommentRepository.findById(entity.getParentComment().getId()).orElse(null);

            if (parent != null) {
                // 若父留言也有父留言，把這次新增的留言的父留言，設定為祖父留言(放到跟本來指定的父留言同一層)
                if (parent.getParentComment() != null) {
                    entity.setParentComment(parent.getParentComment());
                } else {
                    // 否則維持原本的父留言
                    entity.setParentComment(parent);
                }
            } else {
                // 傳入的父留言 ID 無效 → 設為 null（安全處理）
                entity.setParentComment(null);
            }
        } else {
            // 沒傳入父留言 → 明確設為 null
            entity.setParentComment(null);
        }
        return bulletinCommentRepository.save(entity);
    }

    public BulletinComment modify(BulletinComment entity) {

        // 確認留言存在，若不存在則回傳null
        Optional<BulletinComment> optional = bulletinCommentRepository.findById(entity.getId());
        if (optional.isEmpty()) {
            log.warn("欲修改的留言不存在");
            return null;
        }
        BulletinComment original = optional.get();
        if (original.getBulletin().getId() != entity.getBulletin().getId()) {
            log.warn("使用者提供的留言對應到的公告與原資料不符");
            return null;
        }
        if (original.getUser().getUsersId() != entity.getUser().getUsersId()) {
            log.warn("修改人非原留言人");
            return null;
        }

        if (Boolean.FALSE.equals(entity.getIsAlive())) { // 如果要刪除留言，則刪除其所有子留言
            List<BulletinComment> replies = bulletinCommentRepository.findByParentComment_Id(entity.getId());
            replies.forEach(reply -> reply.setIsAlive(false));
            bulletinCommentRepository.saveAll(replies);
        }

        original.setTime(LocalDateTime.now());

        return bulletinCommentRepository.save(original);

    }

    public void deleteById(Integer id) {
        bulletinCommentRepository.deleteById(id);
    }
}
