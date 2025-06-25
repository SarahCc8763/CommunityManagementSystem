package finalProj.service.poll;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.poll.PollVote;
import finalProj.repository.poll.PollVoteRepository;

@Service
@Transactional
public class PollVoteService {

    @Autowired
    private PollVoteRepository pollVoteRepository;

    public List<PollVote> findAll() {
        return pollVoteRepository.findAll();
    }

    public Optional<PollVote> findById(Integer id) {
        return pollVoteRepository.findById(id);
    }

    public PollVote save(PollVote entity) {
        return pollVoteRepository.save(entity);
    }

    public void deleteById(Integer id) {
        pollVoteRepository.deleteById(id);
    }
}
