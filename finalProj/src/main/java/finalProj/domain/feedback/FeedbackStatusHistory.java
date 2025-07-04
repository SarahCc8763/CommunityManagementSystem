package finalProj.domain.feedback;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import finalProj.domain.users.Users;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "feedback_status_history")
public class FeedbackStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_status_history_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "feedback_id")
    @JsonBackReference("feedback-status-history")
    private Feedback feedback;

    @Column(name = "feedback_status_history_old_status")
    private String oldStatus;

    @Column(name = "feedback_status_history_new_status", nullable = false)
    private String newStatus;

    @ManyToOne
    @JoinColumn(name = "changed_by_user_id")
    @JsonBackReference("feedbackStatusHistory-user")
    private Users changedBy;

    @Column(name = "feedback_status_history_changed_at", insertable = false)
    private LocalDateTime changedAt;

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

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public Users getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(Users changedBy) {
        this.changedBy = changedBy;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }

    @Override
    public String toString() {
        return "FeedbackStatusHistory{" +
                "id=" + id +
                ", oldStatus='" + oldStatus + "'" +
                ", newStatus='" + newStatus + "'" +
                ", changedAt=" + changedAt +
                '}';
    }
}
