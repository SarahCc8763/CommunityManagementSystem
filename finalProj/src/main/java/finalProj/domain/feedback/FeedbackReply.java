package finalProj.domain.feedback;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import finalProj.domain.User;

@Entity
@Table(name = "feedback_reply")
public class FeedbackReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_reply_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "feedback_reply", nullable = false, length = 2000)
    private String reply;

    @Column(name = "feedback_reply_replied_at", insertable = false)
    private LocalDateTime repliedAt;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public LocalDateTime getRepliedAt() {
        return repliedAt;
    }

    public void setRepliedAt(LocalDateTime repliedAt) {
        this.repliedAt = repliedAt;
    }

    @Override
    public String toString() {
        return "FeedbackReply{" +
                "id=" + id +
                ", reply='" + reply + "'" +
                ", repliedAt=" + repliedAt +
                '}';
    }
}
