package finalProj.service.poll;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.poll.Poll;
import finalProj.domain.poll.PollVote;
import finalProj.domain.users.Users;
import finalProj.repository.poll.PollOptionRepository;
import finalProj.repository.poll.PollRepository;
import finalProj.repository.poll.PollVoteRepository;
import finalProj.repository.users.UsersRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
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

    /*
     * public PollVote vote(PollVote vote) {
     * 
     * Optional<PollOption> optionalOption =
     * pollOptionRepository.findById(vote.getOption().getId());
     * Optional<Users> optionalUser = usersRepository.findById(vote.getVoteUser());
     * Optional<Poll> optionalPoll = pollRepository.findById(vote.getPollId());
     * 
     * if (optionalOption.isEmpty() || optionalUser.isEmpty() ||
     * optionalPoll.isEmpty()) {
     * 
     * log.warn("找不到選項{}、使用者{}或投票{}", vote.getOption().getId(), vote.getVoteUser(),
     * vote.getPollId());
     * return null;
     * }
     * 
     * PollOption option = optionalOption.get();
     * Users user = optionalUser.get();
     * Poll poll = optionalPoll.get();
     * 
     * // 如果投票已截止，應已透過前端阻攔
     * if (poll.getEnd().isBefore(LocalDateTime.now())) {
     * log.info("投票已截止");
     * return null;
     * }
     * 
     * // 檢查該選項是否屬於該投票
     * if (!option.getPoll().getId().equals(poll.getId())) {
     * log.warn("選項不屬於該投票");
     * return null;
     * }
     * 
     * boolean isMultiple = poll.getIsMultiple(); // 或 poll.isMultiple()，依照你的實體類別為主
     * 
     * // 查詢使用者是否已對該選項投票
     * Optional<PollVote> existingVoteOpt =
     * pollVoteRepository.findByUser_UsersIdAndOption_Id(user.getUsersId(),
     * option.getId());
     * 
     * if (isMultiple) {
     * // 複選邏輯：toggle 該選項
     * if (existingVoteOpt.isPresent()) {
     * PollVote oldVote = existingVoteOpt.get();
     * log.info("已經投票過，切換 已投票/未投票 狀態");
     * oldVote.setIsChecked(!oldVote.getIsChecked());
     * return pollVoteRepository.save(oldVote);
     * } else {
     * PollVote newVote = new PollVote();
     * newVote.setUser(user);
     * newVote.setOption(option);
     * newVote.setPoll(poll);
     * newVote.setIsChecked(true);
     * return pollVoteRepository.save(newVote);
     * }
     * } else {
     * // 先找這個選項的舊投票
     * PollVote voteToSave = existingVoteOpt.orElse(null);
     * 
     * // 1. 如果有投票紀錄（即使 isChecked 為 false）：
     * if (voteToSave != null) {
     * boolean currentStatus = voteToSave.getIsChecked();
     * 
     * if (currentStatus) {
     * // 已勾選 → 取消勾選
     * voteToSave.setIsChecked(false);
     * log.info("取消投票：{}", option.getText());
     * return pollVoteRepository.save(voteToSave);
     * } else {
     * // 未勾選 → 先取消所有其他選項
     * List<PollVote> allVotes =
     * pollVoteRepository.findByUser_UsersIdAndPoll_Id(user.getUsersId(),
     * poll.getId());
     * for (PollVote pv : allVotes) {
     * if (pv.getIsChecked()) {
     * pv.setIsChecked(false);
     * }
     * }
     * pollVoteRepository.saveAll(allVotes);
     * 
     * // 勾選目前的選項
     * voteToSave.setIsChecked(true);
     * log.info("重新投票：{}", option.getText());
     * return pollVoteRepository.save(voteToSave);
     * }
     * }
     * 
     * // 2. 沒投過該選項 → 一樣先取消所有舊選項
     * List<PollVote> allVotes =
     * pollVoteRepository.findByUser_UsersIdAndPoll_Id(user.getUsersId(),
     * poll.getId());
     * for (PollVote pv : allVotes) {
     * if (pv.getIsChecked()) {
     * pv.setIsChecked(false);
     * }
     * }
     * pollVoteRepository.saveAll(allVotes);
     * 
     * // 建立新的投票
     * PollVote newVote = new PollVote();
     * newVote.setUser(user);
     * newVote.setOption(option);
     * newVote.setPoll(poll);
     * newVote.setIsChecked(true);
     * log.info("新投票：{}", option.getText());
     * return pollVoteRepository.save(newVote);
     * 
     * }
     * }
     */

    public void saveVotes(Integer pollId, Integer userId, List<Integer> selectedOptionIds) {

        Optional<Users> optionalUser = usersRepository.findById(userId);
        Optional<Poll> optionalPoll = pollRepository.findById(pollId);

        if (optionalUser.isEmpty() || optionalPoll.isEmpty()) {
            throw new RuntimeException("使用者或投票不存在");
        }

        Users user = optionalUser.get();
        Poll poll = optionalPoll.get();

        // 找出所有舊的投票紀錄
        List<PollVote> allVotes = pollVoteRepository.findByUser_UsersIdAndPoll_Id(user.getUsersId(), poll.getId());

        // 建立 map 方便快速查找
        Map<Integer, PollVote> voteMap = allVotes.stream()
                .collect(Collectors.toMap(v -> v.getOption().getId(), v -> v));

        // 清空所有勾選
        for (PollVote vote : allVotes) {
            vote.setIsChecked(false);
        }

        // 更新勾選狀態
        for (Integer optId : selectedOptionIds) {
            PollVote vote = voteMap.get(optId);
            if (vote == null) {
                vote = new PollVote();
                vote.setUser(user);
                vote.setPoll(poll);
                vote.setOption(pollOptionRepository.findById(optId)
                        .orElseThrow(() -> new RuntimeException("選項不存在")));
                allVotes.add(vote);
            }
            vote.setIsChecked(true);
        }

        // 統一保存
        pollVoteRepository.saveAll(allVotes);
        log.info("投票紀錄已更新，投票人ID:{} -> {}", user.getUsersId(), allVotes.stream().map(v -> {
            return v.getOption().getText() + ": " + v.getIsChecked();
        }).collect((Collectors.joining(", "))));
    }

    public List<PollVote> findByUsersIdAndPollId(Integer usersId, Integer pollId) {
        return pollVoteRepository.findByUser_UsersIdAndPoll_Id(usersId, pollId);
    }
}
