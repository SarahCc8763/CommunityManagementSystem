package finalProj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProj.domain.Bulletin;
import finalProj.repository.BulletinRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class BulletinService {
	
	@Autowired
    private BulletinRepository repository;


    public List<Bulletin> findAll() {
        return repository.findAll();
    }

    public Bulletin save(Bulletin apply) {
        return repository.save(apply);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public Bulletin findById(Integer id) {
        return repository.findById(id).orElse(null);
    }
}
