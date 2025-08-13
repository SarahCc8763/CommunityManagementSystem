package finalProj.domain.faq;

import com.fasterxml.jackson.annotation.JsonBackReference;

import finalProj.domain.embed.FaqFaqKeywordId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "faq_faq_keyword")
public class FaqFaqKeyword {

    @EmbeddedId
    private FaqFaqKeywordId id;

    @ManyToOne
    @MapsId("faqId")
    @JoinColumn(name = "faq_id")
    @JsonBackReference("faq-keyword")
    private Faq faq;

    @ManyToOne
    @MapsId("faqKeywordId")
    @JoinColumn(name = "faq_keyword_id")
    @JsonBackReference("keyword-faq")
    private FaqKeyword keyword;

    public FaqFaqKeyword() {

    }

    public FaqFaqKeyword(FaqFaqKeywordId id, Faq faq, FaqKeyword keyword) {
        this.id = id;
        this.faq = faq;
        this.keyword = keyword;
    }

    public FaqFaqKeywordId getId() {
        return id;
    }

    public void setId(FaqFaqKeywordId id) {
        this.id = id;
    }

    public Faq getFaq() {
        return faq;
    }

    public void setFaq(Faq faq) {
        this.faq = faq;
    }

    public FaqKeyword getKeyword() {
        return keyword;
    }

    public void setKeyword(FaqKeyword keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "FaqFaqKeyword{" +
                "id=" + id +
                ", faq=" + (faq != null ? faq.getId() : null) +
                ", keyword=" + (keyword != null ? keyword.getId() : null) +
                '}';
    }
}
