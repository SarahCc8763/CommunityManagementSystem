package finalProj.service.bulletin;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.bulletin.Bulletin;
import finalProj.domain.bulletin.BulletinComment;
import finalProj.domain.users.Users;
import finalProj.repository.bulletin.BulletinCommentRepository;
import finalProj.repository.users.UsersRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class BulletinCommentService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BulletinService bulletinService;

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
        // 確認使用者存在，若不存在則回傳null
        // -->之後要改成用service
        Users existUser = usersRepository.findById(entity.getUser().getUsersId()).orElse(null);
        if (existUser == null) {
            log.warn("使用者不存在");
            return null;
        }
        // 確認公告存在，若不存在則回傳null
        Bulletin bulletin = bulletinService.findById(entity.getBulletin().getId());
        if (bulletin == null) {
            log.warn("公告不存在");
            return null;
        }
        // 確認留言存在，若不存在則回傳null
        Optional<BulletinComment> optional = bulletinCommentRepository.findById(entity.getId());
        if (optional.isEmpty()) {
            log.warn("欲修改的留言不存在");
            return null;
        }
        BulletinComment parentComment = optional.get().getParentComment();
        entity.setParentComment(parentComment); // 父留言不變
        entity.setUser(existUser); // 使用者不變
        entity.setBulletin(bulletin); // 對應到的公告不變
        if (Boolean.FALSE.equals(entity.getIsAlive())) { // 如果要刪除留言，則刪除其所有子留言
            List<BulletinComment> replies = bulletinCommentRepository.findByParentComment_Id(entity.getId());
            replies.forEach(reply -> reply.setIsAlive(false));
            bulletinCommentRepository.saveAll(replies);
        }

        entity.setTime(LocalDateTime.now());

        return bulletinCommentRepository.save(entity);

    }

    public void deleteById(Integer id) {
        bulletinCommentRepository.deleteById(id);
    }
}
