package finalProj.service.poll;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.poll.PollOption;
import finalProj.repository.poll.PollOptionRepository;

@Service
@Transactional
public class PollOptionService {

    @Autowired
    private PollOptionRepository pollOptionRepository;

    public List<PollOption> findAll() {
        return pollOptionRepository.findAll();
    }

    public Optional<PollOption> findById(Integer id) {
        return pollOptionRepository.findById(id);
    }

    public PollOption save(PollOption entity) {
        return pollOptionRepository.save(entity);
    }

    public void deleteById(Integer id) {
        pollOptionRepository.deleteById(id);
    }
}
