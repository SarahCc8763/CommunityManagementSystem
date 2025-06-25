
package finalProj.domain;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.*;

import finalProj.domain.bulletin.Bulletin;
import finalProj.domain.bulletin.BulletinComment;
import finalProj.domain.bulletin.BulletinCommentLike;
import finalProj.domain.feedback.Feedback;
import finalProj.domain.feedback.FeedbackStatusHistory;
import finalProj.domain.poll.PollVote;

import java.util.*;

@Entity
@Table(name = "[user]")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username", length = 50)
    private String username;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference("feedback-user")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference("bulletinComment-user")
    private List<BulletinComment> bulletinComments;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference("bulletinCommentLike-user")
    private List<BulletinCommentLike> bulletinCommentLikes;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference("pollVote-user")
    private List<PollVote> pollVotes;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference("bulletin-user")
    private List<Bulletin> bulletins;

    @OneToMany(mappedBy = "handler")
    @JsonManagedReference("feedback-handler")
    private List<Feedback> handledFeedbacks;

    @OneToMany(mappedBy = "changedBy")
    @JsonManagedReference("feedbackStatusHistory-user")
    private List<FeedbackStatusHistory> feedbackStatusHistories;

    @Override
    public String toString() {
        return "User [userId=" + userId + ", username=" + username + ", feedbacks=" + feedbacks + ", bulletinComments="
                + bulletinComments + ", bulletinCommentLikes=" + bulletinCommentLikes + ", pollVotes=" + pollVotes
                + ", handledFeedbacks=" + handledFeedbacks + ", feedbackStatusHistories=" + feedbackStatusHistories
                + "]";
    }

    public List<BulletinComment> getBulletinComments() {
        return bulletinComments;
    }

    public void setBulletinComments(List<BulletinComment> bulletinComments) {
        this.bulletinComments = bulletinComments;
    }

    public List<BulletinCommentLike> getBulletinCommentLikes() {
        return bulletinCommentLikes;
    }

    public void setBulletinCommentLikes(List<BulletinCommentLike> bulletinCommentLikes) {
        this.bulletinCommentLikes = bulletinCommentLikes;
    }

    public List<PollVote> getPollVotes() {
        return pollVotes;
    }

    public void setPollVotes(List<PollVote> pollVotes) {
        this.pollVotes = pollVotes;
    }

    public List<FeedbackStatusHistory> getFeedbackStatusHistories() {
        return feedbackStatusHistories;
    }

    public void setFeedbackStatusHistories(List<FeedbackStatusHistory> feedbackStatusHistories) {
        this.feedbackStatusHistories = feedbackStatusHistories;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public List<Feedback> getHandledFeedbacks() {
        return handledFeedbacks;
    }

    public void setHandledFeedbacks(List<Feedback> handledFeedbacks) {
        this.handledFeedbacks = handledFeedbacks;
    }
}
