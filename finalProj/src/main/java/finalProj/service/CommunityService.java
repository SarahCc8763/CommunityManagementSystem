package finalProj.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.community.Community;
import finalProj.repository.CommunityRepository;

@Service
@Transactional
public class CommunityService {

    @Autowired
    private CommunityRepository communityRepository;

    public List<Community> findAll() {
        return communityRepository.findAll();
    }

    public Optional<Community> findById(Integer id) {
        return communityRepository.findById(id);
    }

    public Community save(Community entity) {
        return communityRepository.save(entity);
    }

    public void deleteById(Integer id) {
        communityRepository.deleteById(id);
    }
}
