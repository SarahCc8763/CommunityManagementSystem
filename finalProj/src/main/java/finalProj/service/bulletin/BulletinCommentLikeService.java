package finalProj.service.bulletin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.bulletin.BulletinComment;
import finalProj.domain.bulletin.BulletinCommentLike;
import finalProj.domain.embed.BulletinCommentLikeId;
import finalProj.domain.users.Users;
import finalProj.repository.bulletin.BulletinCommentLikeRepository;
import finalProj.repository.bulletin.BulletinCommentRepository;
import finalProj.repository.users.UsersRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class BulletinCommentLikeService {

    @Autowired
    private BulletinCommentLikeRepository bulletinCommentLikeRepository;

    @Autowired
    private BulletinCommentRepository bulletinCommentRepository;

    @Autowired
    private UsersRepository userRepository;

    public List<BulletinCommentLike> findAll() {
        return bulletinCommentLikeRepository.findAll();
    }

    public BulletinCommentLike save(BulletinCommentLike entity) {
        return bulletinCommentLikeRepository.save(entity);
    }

    public BulletinCommentLike likeComment(Integer commentId, Integer userId) {
        // 檢查是否已經按過讚
        BulletinCommentLikeId likeId = new BulletinCommentLikeId(commentId, userId);
        Optional<BulletinCommentLike> optional = bulletinCommentLikeRepository.findById(likeId);

        if (optional.isPresent()) { // 已經按過讚
            Boolean validStatus = Boolean.TRUE.equals(optional.get().getIsValid()); // 取得當前按讚狀態
            optional.get().setIsValid(!validStatus); // 切換按讚狀態
            log.info("當前的讚為{}", !validStatus);
            return bulletinCommentLikeRepository.save(optional.get()); // 更新狀態
        }

        // 查出關聯的 Comment 和 User 實體
        BulletinComment comment = bulletinCommentRepository.findById(commentId).get();
        Users user = userRepository.findById(userId).get();

        // 建立按讚紀錄
        BulletinCommentLike like = new BulletinCommentLike();
        like.setId(likeId); // 設定複合主鍵
        like.setComment(comment); // 設定關聯留言
        like.setUser(user); // 設定關聯使用者
        like.setIsValid(true); // 設定有效
        log.info("按讚成功");
        return bulletinCommentLikeRepository.save(like);
    }
}
