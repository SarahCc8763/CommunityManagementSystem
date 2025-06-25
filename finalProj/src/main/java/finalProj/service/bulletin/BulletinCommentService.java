package finalProj.service.bulletin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.bulletin.BulletinComment;
import finalProj.repository.bulletin.BulletinCommentRepository;

@Service
@Transactional
public class BulletinCommentService {

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

    public void deleteById(Integer id) {
        bulletinCommentRepository.deleteById(id);
    }
}
