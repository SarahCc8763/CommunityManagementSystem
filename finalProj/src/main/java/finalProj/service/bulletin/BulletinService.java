package finalProj.service.bulletin;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.bulletin.Bulletin;
import finalProj.domain.bulletin.BulletinCategory;
import finalProj.repository.bulletin.BulletinCategoryRepository;
import finalProj.repository.bulletin.BulletinRepository;

@Service
@Transactional
public class BulletinService {

    @Autowired
    private BulletinRepository bulletinRepository;

    @Autowired
    private BulletinCategoryRepository bulletinCategoryRepository;

    public List<Bulletin> findAll() {
        return bulletinRepository.findAll();
    }

    public List<Bulletin> findById(Integer id) {
        Optional<Bulletin> optional = bulletinRepository.findById(id);
        if (optional.isPresent()) {
            return List.of(optional.get());
        }

        return null;
    }

    public Bulletin insert(Bulletin entity) {
        if (entity != null) {
            try {
                Optional<BulletinCategory> optional = bulletinCategoryRepository
                        .findByName(entity.getCategory().getName());
                if (!optional.isPresent()) {
                    return null;
                }
                entity.setCategory(optional.get());
                return bulletinRepository.save(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Bulletin modify(Bulletin entity) {
        if (entity != null) {
            try {
                entity.setModifyTime(LocalDateTime.now());
                return bulletinRepository.save(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean deleteById(Integer id) {
        if (id != null) {
            try {
                if (bulletinRepository.existsById(id)) {
                    bulletinRepository.deleteById(id);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Boolean existsById(Integer id) {
        return bulletinRepository.existsById(id);
    }

    public List<Bulletin> findByCategoryAndTitle(String category, String title) {

        return bulletinRepository.findByCategoryAndTitle(category, title);
    }
}
