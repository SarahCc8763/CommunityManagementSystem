package finalProj.controller.poll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.poll.Poll;
import finalProj.domain.poll.PollVote;
import finalProj.service.poll.PollService;
import finalProj.service.poll.PollVoteService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/poll")
@Slf4j
//
// 發布公告同時新增投票的controller方在BulletinController的新增公告方法裡面
//
public class PollController {

    @Autowired
    private PollService pollService;

    @Autowired
    private PollVoteService pollVoteService;

    // 更新投票
    //
    // 提醒! 不提供修改或刪除既有的舊選項，只提供新增選項，
    // 所以前端的 request body請不要傳舊選項的資料，如果舊選項沒同時提供該選項原ID，會被當成新選項來新增!新增之後不能刪除
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePoll(@PathVariable Integer id, @RequestBody Poll updatedPoll) {
        Poll result = pollService.updatePoll(id, updatedPoll);

        if (result != null) {
            log.info("投票更新成功，ID: {}", id);
            return ResponseEntity.ok("投票更新成功");
        } else {
            log.warn("投票更新失敗，找不到指定的投票，ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到指定的投票");
        }
    }

    // 新增/修改投票紀錄
    @PostMapping("/{id}/vote")
    public ResponseEntity<?> vote(@PathVariable Integer id, @RequestBody PollVote vote) {
        vote.setPollId(id);
        if (pollVoteService.vote(vote) != null) {
            return ResponseEntity.ok("投票成功");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("投票資料有誤");
        }
    }

    @DeleteMapping("/delete/{pollId}")
    public ResponseEntity<?> deletePoll(@PathVariable Integer pollId) {
        pollService.deleteById(pollId);
        return ResponseEntity.ok("投票刪除成功");
    }
}
