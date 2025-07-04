package finalProj.repository.poll;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.poll.PollOption;

@Repository
public interface PollOptionRepository extends JpaRepository<PollOption, Integer> {

}
