package finalProj.repository.poll;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.poll.PollVote;

@Repository
public interface PollVoteRepository extends JpaRepository<PollVote, Integer> {
    public Optional<PollVote> findByUser_UsersIdAndOption_Id(int userId, int optionId);

    public List<PollVote> findByUser_UsersIdAndPoll_Id(Integer userId, Integer pollId);

}
