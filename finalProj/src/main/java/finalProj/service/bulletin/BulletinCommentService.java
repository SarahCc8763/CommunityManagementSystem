package finalProj.service.bulletin;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.bulletin.BulletinComment;
import finalProj.repository.bulletin.BulletinCommentRepository;
import finalProj.repository.users.UsersRepository;

@Service
@Transactional
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
        return bulletinCommentRepository.save(entity);
    }

    public BulletinComment modify(BulletinComment entity) {

        entity.setTime(LocalDateTime.now());
        entity.setUser(usersRepository.findById(entity.getUser().getUsersId()).get()); // 資料庫抓取完整用戶資料放進關聯屬性user
        entity.setBulletin(bulletinService.findById(entity.getBulletin().getId())); // 資料庫抓取完整公告資料放進關聯屬性bulletin

        return bulletinCommentRepository.save(entity);
    }

    public void deleteById(Integer id) {
        bulletinCommentRepository.deleteById(id);
    }
}
