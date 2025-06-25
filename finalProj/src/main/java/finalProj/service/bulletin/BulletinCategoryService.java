package finalProj.service.bulletin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.bulletin.BulletinCategory;
import finalProj.repository.bulletin.BulletinCategoryRepository;

@Service
@Transactional
public class BulletinCategoryService {

    @Autowired
    private BulletinCategoryRepository bulletinCategoryRepository;

    public List<BulletinCategory> findAll() {
        return bulletinCategoryRepository.findAll();
    }

    public Optional<BulletinCategory> findByName(String name) {
        return bulletinCategoryRepository.findByName(name);
    }

    public BulletinCategory save(BulletinCategory entity) {
        return bulletinCategoryRepository.save(entity);
    }

    public void deleteByName(String name) {

        bulletinCategoryRepository.deleteByName(name);

    }
}
