package finalProj.service.poll;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.poll.Poll;
import finalProj.domain.poll.PollOption;
import finalProj.domain.poll.PollVote;
import finalProj.domain.users.Users;
import finalProj.repository.poll.PollOptionRepository;
import finalProj.repository.poll.PollRepository;
import finalProj.repository.poll.PollVoteRepository;
import finalProj.repository.users.UsersRepository;

@Service
@Transactional
public class PollVoteService {
    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollVoteRepository pollVoteRepository;

    @Autowired
    private PollOptionRepository pollOptionRepository;

    @Autowired
    private UsersRepository usersRepository;

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

    public PollVote vote(PollVote vote) {

        Optional<PollOption> optionalOption = pollOptionRepository.findById(vote.getOption().getId());
        Optional<Users> optionalUser = usersRepository.findById(vote.getVoteUser());
        Optional<Poll> optionalPoll = pollRepository.findById(vote.getPollId());

        if (optionalOption.isEmpty() || optionalUser.isEmpty() || optionalPoll.isEmpty()) {
            return null;
        }

        PollOption option = optionalOption.get();
        Users user = optionalUser.get();
        Poll poll = optionalPoll.get();

        // 如果投票已截止，應已透過前端阻攔
        if (poll.getEnd().isBefore(LocalDateTime.now())) {
            System.out.println("投票已截止");
            return null;
        }

        // 檢查該選項是否屬於該投票
        if (!option.getPoll().getId().equals(poll.getId())) {
            System.out.println("該選項不屬於該投票");
            return null;
        }

        boolean isMultiple = poll.getIsMultiple(); // 或 poll.isMultiple()，依照你的實體類別為主

        // 查詢使用者是否已對該選項投票
        Optional<PollVote> existingVoteOpt = pollVoteRepository.findByUser_UsersIdAndOption_Id(user.getUsersId(),
                option.getId());

        if (isMultiple) {
            // 複選邏輯：toggle 該選項
            if (existingVoteOpt.isPresent()) {
                PollVote oldVote = existingVoteOpt.get();
                oldVote.setIsChecked(false);
                return pollVoteRepository.save(oldVote);
            } else {
                PollVote newVote = new PollVote();
                newVote.setUser(user);
                newVote.setOption(option);
                newVote.setPoll(poll);
                newVote.setIsChecked(true);
                return pollVoteRepository.save(newVote);
            }
        } else {
            // 單選邏輯：

            // 1. 現有已勾選的先全部取消
            List<PollVote> allVotes = pollVoteRepository.findByUser_UsersIdAndPoll_Id(user.getUsersId(), poll.getId());
            for (PollVote pv : allVotes) {
                if (pv.getIsChecked()) {
                    pv.setIsChecked(false);
                    pollVoteRepository.save(pv);
                }
            }

            // 2. 若這次點的是本來已經選過的 → 因為上面已經取消了，所以不用再傳
            if (existingVoteOpt.isPresent() && existingVoteOpt.get().getIsChecked()) {
                return null; // 表示「取消投票」
            }

            // 3. 沒投過，或剛剛取消過 → 勾選這個選項
            PollVote voteToSave = existingVoteOpt.orElse(new PollVote());
            voteToSave.setUser(user);
            voteToSave.setOption(option);
            voteToSave.setPoll(poll);
            voteToSave.setIsChecked(true);
            return pollVoteRepository.save(voteToSave);

        }
    }

}
