package finalProj.repository.poll;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.poll.PollVote;

@Repository
public interface PollVoteRepository extends JpaRepository<PollVote, Integer> {

}
