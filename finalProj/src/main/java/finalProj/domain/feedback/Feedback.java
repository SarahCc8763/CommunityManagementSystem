
package finalProj.domain.feedback;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import finalProj.domain.community.Community;
import finalProj.domain.users.Users;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "feedback")
// @JsonIgnoreProperties({"handler", "community", "attachments",
// "statusHistories", "replies"})
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Integer id;

    @Column(name = "feedback_title", nullable = false)
    private String title;

    @Column(name = "feedback_description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "feedback_category_id")
    // @JsonBackReference("feedback-category")
    private FeedbackCategory category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("feedback-user")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "handler_id")
    @JsonBackReference("feedback-handler")
    // @JsonIgnoreProperties("feedback-handler")
    private Users handler;

    @Column(name = "feedback_submit_at", insertable = false)
    private LocalDateTime submittedAt;

    @Column(name = "feedback_status")
    private String status;

    @Column(name = "feedback_last_updated_at", insertable = false)
    private LocalDateTime lastUpdated;

    @Column(name = "feedback_resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "feedback_user_rating")
    private Byte userRating;

    @ManyToOne
    @JoinColumn(name = "community_id")
    @JsonBackReference("community-feedback")
    private Community community;

    @OneToMany(mappedBy = "feedback", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("feedback-attachment")
    private List<FeedbackAttachment> attachments;

    @OneToMany(mappedBy = "feedback")
    @JsonManagedReference("feedback-status-history")
    private List<FeedbackStatusHistory> statusHistories;

    @OneToMany(mappedBy = "feedback", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("feedback-reply")
    private List<FeedbackReply> replies;

    @Override
    public String toString() {
        return "Feedback [id=" + id + ", title=" + title + ", description=" + description + ", category=" + category
                + ", user=" + user + ", handler=" + handler + ", submittedAt=" + submittedAt + ", status=" + status
                + ", lastUpdated=" + lastUpdated + ", resolvedAt=" + resolvedAt + ", userRating=" + userRating
                + ", community=" + community + ", attachments=" + attachments + ", statusHistories=" + statusHistories
                + ", replies=" + replies + "]";
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public List<FeedbackAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<FeedbackAttachment> attachments) {
        this.attachments = attachments;
    }

    public List<FeedbackStatusHistory> getStatusHistories() {
        return statusHistories;
    }

    public void setStatusHistories(List<FeedbackStatusHistory> statusHistories) {
        this.statusHistories = statusHistories;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FeedbackCategory getCategory() {
        return category;
    }

    public void setCategory(FeedbackCategory category) {
        this.category = category;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Users getHandler() {
        return handler;
    }

    public void setHandler(Users handler) {
        this.handler = handler;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public Byte getUserRating() {
        return userRating;
    }

    public void setUserRating(Byte userRating) {
        this.userRating = userRating;
    }

    public List<FeedbackReply> getReplies() {
        return replies;
    }

    public void setReplies(List<FeedbackReply> replies) {
        this.replies = replies;
    }
}
