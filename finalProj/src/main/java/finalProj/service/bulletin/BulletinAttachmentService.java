package finalProj.service.bulletin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.bulletin.BulletinAttachment;
import finalProj.repository.bulletin.BulletinAttachmentRepository;

@Service
@Transactional
public class BulletinAttachmentService {

    @Autowired
    private BulletinAttachmentRepository bulletinAttachmentRepository;

    public List<BulletinAttachment> findAll() {
        return bulletinAttachmentRepository.findAll();
    }

    public Optional<BulletinAttachment> findById(Integer id) {
        return bulletinAttachmentRepository.findById(id);
    }

    public BulletinAttachment save(BulletinAttachment entity) {
        return bulletinAttachmentRepository.save(entity);
    }

    public void deleteById(Integer id) {
        bulletinAttachmentRepository.deleteById(id);
    }
}
