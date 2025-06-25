package finalProj.service.poll;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.poll.Poll;
import finalProj.repository.poll.PollRepository;

@Service
@Transactional
public class PollService {

    @Autowired
    private PollRepository pollRepository;

    public List<Poll> findAll() {
        return pollRepository.findAll();
    }

    public Optional<Poll> findById(Integer id) {
        return pollRepository.findById(id);
    }

    public Poll save(Poll entity) {
        return pollRepository.save(entity);
    }

    public void deleteById(Integer id) {
        pollRepository.deleteById(id);
    }
}
