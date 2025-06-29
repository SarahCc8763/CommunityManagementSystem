package finalProj.service.feedback;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.feedback.FeedbackReply;
import finalProj.domain.users.Users;
import finalProj.repository.feedback.FeedbackReplyRepository;
import finalProj.repository.users.UsersRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class FeedbackReplyService {

    @Autowired
    private FeedbackReplyRepository feedbackReplyRepository;
    @Autowired
    private UsersRepository usersRepository;

    public List<FeedbackReply> findAll() {
        return feedbackReplyRepository.findAll();
    }

    public Optional<FeedbackReply> findById(Integer id) {
        return feedbackReplyRepository.findById(id);
    }

    // public FeedbackReply insert(FeedbackReply entity) {
    // entity.getFeedback().setStatus(entity.getFeedbackStatus());
    // return feedbackReplyRepository.save(entity);
    // }

    public FeedbackReply modify(FeedbackReply entity) {
        Optional<Users> optional = usersRepository.findById(entity.getUser().getUsersId());
        if (optional.isEmpty()) {
            log.error("使用者資料有誤");
            return null;
        }
        Users user = optional.get();
        entity.setUser(user);
        entity.setRepliedAt(LocalDateTime.now());
        return feedbackReplyRepository.save(entity);

    }

    public void deleteById(Integer id) {
        feedbackReplyRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return feedbackReplyRepository.existsById(id);
    }
}
