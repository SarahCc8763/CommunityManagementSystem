
package finalProj.domain.faq;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "faq_keyword")
public class FaqKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_keyword_id")
    private Integer id;

    @Column(name = "faq_keyword", nullable = false, length = 50)
    private String word;

    @OneToMany(mappedBy = "keyword")
    @JsonManagedReference("keyword-faq")
    private List<FaqFaqKeyword> faqLinks;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<FaqFaqKeyword> getFaqLinks() {
        return faqLinks;
    }

    public void setFaqLinks(List<FaqFaqKeyword> faqLinks) {
        this.faqLinks = faqLinks;
    }

    @Override
    public String toString() {
        return "Keyword{" + "id=" + id + ", word='" + word + "'" + '}';
    }
}
