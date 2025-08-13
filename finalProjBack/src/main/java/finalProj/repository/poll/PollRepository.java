package finalProj.repository.poll;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.poll.Poll;

@Repository
public interface PollRepository extends JpaRepository<Poll, Integer> {

}
