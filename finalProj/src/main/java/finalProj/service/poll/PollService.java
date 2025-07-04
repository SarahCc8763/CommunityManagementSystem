package finalProj.service.poll;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.poll.Poll;
import finalProj.domain.poll.PollOption;
import finalProj.repository.poll.PollRepository;

@Service
@Transactional
public class PollService {

    @Autowired
    private PollRepository pollRepository;

    // @Autowired
    // private PollOptionRepository pollOptionRepository;

    public List<Poll> findAll() {
        return pollRepository.findAll();
    }

    public Optional<Poll> findById(Integer id) {
        return pollRepository.findById(id);
    }

    public Poll save(Poll entity) {
        return pollRepository.save(entity);
    }

    public void deleteById(Integer id) {
        pollRepository.deleteById(id);
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

        poll.getOptions().clear(); // 全量取代較簡單
        for (PollOption opt : updatedPoll.getOptions()) {
            opt.setPoll(poll);
            poll.getOptions().add(opt);
        }
        return pollRepository.saveAndFlush(poll);
    }

}
