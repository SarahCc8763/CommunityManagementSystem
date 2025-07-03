package finalProj.domain.faq;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import finalProj.domain.community.Community;
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
import jakarta.persistence.Transient;

@Entity
@Table(name = "faq")
public class Faq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_id")
    private Integer id;

    @Column(name = "faq_question", nullable = false, length = 50)
    private String question;

    @Column(name = "faq_answer", nullable = false)
    private String answer;

    @ManyToOne
    @JoinColumn(name = "faq_category_id")
    @JsonBackReference("faq-category")
    private FaqCategory category;

    @Column(name = "faq_last_modified", insertable = false)
    private LocalDateTime lastModified;

    @Column(name = "faq_post_status", insertable = false)
    private Boolean postStatus;

    @OneToMany(mappedBy = "faq", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("faq-keyword")
    private List<FaqFaqKeyword> keywordLinks;

    @ManyToOne
    @JoinColumn(name = "community_id")
    @JsonBackReference("community-faq")
    private Community community;

    @Transient
    private String keywords;

    // --- Getter / Setter ---

    @Override
    public String toString() {
        return "Faq{" +
                "id=" + id +
                ", question='" + question + "'" +
                ", postStatus=" + postStatus +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public FaqCategory getCategory() {
        return category;
    }

    public void setCategory(FaqCategory category) {
        this.category = category;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public Boolean getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(Boolean postStatus) {
        this.postStatus = postStatus;
    }

    public List<FaqFaqKeyword> getKeywordLinks() {
        return keywordLinks;
    }

    public void setKeywordLinks(List<FaqFaqKeyword> keywordLinks) {
        this.keywordLinks = keywordLinks;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }
}
