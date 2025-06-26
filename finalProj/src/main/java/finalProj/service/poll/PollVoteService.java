package finalProj.service.poll;

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
        }
        // else {
        // // 單選邏輯：
        // // 1. 取消其他選項的 isChecked = true
        // List<PollVote> allVotes =
        // pollVoteRepository.findByUser_UsersIdAndPoll_Id(user.getUsersId(),
        // poll.getId());
        // for (PollVote pv : allVotes) {
        // if (pv.getIsChecked()) {
        // pv.setIsChecked(false);
        // pollVoteRepository.save(pv);
        // }
        // }

        // // // 2. 如果剛剛點的是已經存在，就切換選擇狀態
        // // if (existingVoteOpt.isPresent()) {
        // // PollVote oldVote = existingVoteOpt.get();
        // // oldVote.setIsChecked(!existingVoteOpt.get().getIsChecked());
        // // return pollVoteRepository.save(oldVote);
        // // }

        // // // 3. 勾選目前選項（新增或更新）
        // // PollVote voteToSave = existingVoteOpt.orElse(new PollVote());
        // // voteToSave.setUser(user);
        // // voteToSave.setOption(option);
        // // voteToSave.setPoll(poll);
        // // voteToSave.setIsChecked(true);
        // // return pollVoteRepository.save(voteToSave);
        // }
    }

}
