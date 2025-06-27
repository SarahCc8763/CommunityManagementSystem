
package finalProj.domain.feedback;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import finalProj.domain.community.Community;
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
@Table(name = "feedback_category")
public class FeedbackCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_category_id")
    private Integer id;

    @Column(name = "feedback_category_name", nullable = false)
    private String name;

    @Column(name = "feedback_category_description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "community_id")
    @JsonBackReference("community-feedback-category")
    private Community community;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference("feedback-category")
    private List<Feedback> feedbacks;

    @Override
    public String toString() {
        return "FeedbackCategory [id=" + id + ", name=" + name + ", description=" + description + ", community="
                + community + "]";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }
}
