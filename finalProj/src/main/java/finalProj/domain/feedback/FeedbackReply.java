package finalProj.domain.feedback;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import finalProj.domain.users.Users;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "feedback_reply")
public class FeedbackReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_reply_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "feedback_id")
    @JsonBackReference("feedback-reply")
    private Feedback feedback;

    @ManyToOne
    @JoinColumn(name = "user_id")
    // @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Users user;

    @Column(name = "feedback_reply", nullable = false, length = 2000)
    private String reply;

    @Column(name = "feedback_reply_replied_at", insertable = false)
    private LocalDateTime repliedAt;

    @Transient
    private String preFeedBackStatus;

    @Transient
    private String newFeedBackStatus;

    @Transient
    private String userName;

    public String getUserName() {
        if (user != null) {
            userName = user.getName();
        }
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
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

    public String getPreFeedBackStatus() {
        preFeedBackStatus = feedback.getStatus();
        return preFeedBackStatus;
    }

    public void setPreFeedBackStatus(String preFeedBackStatus) {
        this.preFeedBackStatus = preFeedBackStatus;
    }

    public String getNewFeedBackStatus() {
        return newFeedBackStatus;
    }

    public void setNewFeedBackStatus(String newFeedBackStatus) {
        this.newFeedBackStatus = newFeedBackStatus;
    }

}
