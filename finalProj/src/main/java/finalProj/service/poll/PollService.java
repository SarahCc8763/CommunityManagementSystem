package finalProj.service.poll;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.bulletin.Bulletin;
import finalProj.domain.poll.Poll;
import finalProj.domain.poll.PollOption;
import finalProj.repository.poll.PollOptionRepository;
import finalProj.repository.poll.PollRepository;
import finalProj.repository.poll.PollVoteRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class PollService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollOptionRepository pollOptionRepository;

    @Autowired
    private PollVoteRepository pollVoteRepository;

    public List<Poll> findAll() {
        return pollRepository.findAll();
    }

    public Optional<Poll> findById(Integer id) {
        return pollRepository.findById(id);
    }

    public Poll save(Poll entity) {
        return pollRepository.save(entity);
    }

    public Boolean deleteById(Integer id) {
        log.info("準備刪除投票{}", id);

        Optional<Poll> option = pollRepository.findById(id);
        if (option.isPresent()) {
            Poll poll = option.get();

            // 強制初始化關聯
            poll.getOptions().size();
            poll.getVotes().size();

            // 先刪除投票紀錄
            pollVoteRepository.deleteAll(poll.getVotes());
            poll.getVotes().clear();

            // 先刪除 option
            pollOptionRepository.deleteAll(poll.getOptions());
            poll.getOptions().clear();

            // 解除 Bulletin 端關聯
            Bulletin bulletin = poll.getBulletin();
            if (bulletin != null) {
                bulletin.setPoll(null); // 你需要在 Bulletin entity 加 setter
            }

            poll.setBulletin(null); // 雙向解除

            // 再進行刪除

            // 再刪除 poll 本體
            pollRepository.delete(poll);

            log.info("投票{}已刪除", id);
            return true;
        }

        log.warn("投票{}不存在，無法刪除", id);
        return false;
    }

    // public Poll updatePoll(Integer pollId, Poll updatedPoll) {
    // Optional<Poll> optional = pollRepository.findById(pollId);
    // if (!optional.isPresent()) {
    // return null;
    // }

    // Poll existingPoll = optional.get();

    // // 更新投票主標題與多選狀態
    // if (updatedPoll.getTitle() != null) {
    // existingPoll.setTitle(updatedPoll.getTitle());
    // }
    // existingPoll.setIsMultiple(updatedPoll.getIsMultiple());

    // // 判斷要新增的選項（避免重複）
    // if (updatedPoll.getOptions() != null && !updatedPoll.getOptions().isEmpty())
    // {
    // for (PollOption newOption : updatedPoll.getOptions()) {
    // // 若新選項未提供 id，代表是新增
    // if (newOption.getId() == null) {
    // newOption.setPoll(existingPoll);
    // existingPoll.getOptions().add(newOption);
    // }
    // }
    // }

    // return pollRepository.save(existingPoll);
    // }
    public Poll updatePoll(Integer pollId, Poll updatedPoll) {
        Poll poll = pollRepository.findById(pollId).orElse(null);
        if (poll == null)
            return null;

        poll.setTitle(updatedPoll.getTitle());
        poll.setIsMultiple(updatedPoll.getIsMultiple());
        poll.setStart(updatedPoll.getStart());
        poll.setEnd(updatedPoll.getEnd());

        List<PollOption> newOptions = updatedPoll.getOptions();
        List<PollOption> toKeep = new ArrayList<>();
        List<PollOption> toAdd = new ArrayList<>();

        // 1. 保留或新增選項
        for (PollOption newOpt : newOptions) {
            Optional<PollOption> existingOpt = pollOptionRepository.findByPoll_IdAndText(pollId, newOpt.getText());
            if (existingOpt.isPresent()) {
                toKeep.add(existingOpt.get()); // 保留原選項
            } else {
                newOpt.setPoll(poll);
                toAdd.add(newOpt); // 是新選項
            }
        }

        // 2. 先找出要刪除的選項，再從 poll.options 中移除（避免改變整個 List）
        List<PollOption> toRemove = new ArrayList<>();
        for (PollOption existing : poll.getOptions()) {
            if (toKeep.stream().noneMatch(opt -> opt.getText().equals(existing.getText()))) {
                // 只有當沒有被保留時才移除
                toRemove.add(existing);
            }
        }

        poll.getOptions().removeAll(toRemove); // ✅ 這樣不會破壞 reference
        poll.getOptions().addAll(toAdd); // ✅ 安全新增

        return pollRepository.saveAndFlush(poll);
    }

}
