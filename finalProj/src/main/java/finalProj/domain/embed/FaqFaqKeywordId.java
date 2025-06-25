package finalProj.domain.embed;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class FaqFaqKeywordId implements Serializable {

    @Column(name = "faq_id")
    private Integer faqId;

    @Column(name = "faq_keyword_id")
    private Integer faqKeywordId;

    public FaqFaqKeywordId() {
    }

    public FaqFaqKeywordId(Integer faqId, Integer faqKeywordId) {
        this.faqId = faqId;
        this.faqKeywordId = faqKeywordId;
    }

    public Integer getFaqId() {
        return faqId;
    }

    public void setFaqId(Integer faqId) {
        this.faqId = faqId;
    }

    public Integer getFaqKeywordId() {
        return faqKeywordId;
    }

    public void setFaqKeywordId(Integer faqKeywordId) {
        this.faqKeywordId = faqKeywordId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof FaqFaqKeywordId))
            return false;
        FaqFaqKeywordId that = (FaqFaqKeywordId) o;
        return Objects.equals(faqId, that.faqId) && Objects.equals(faqKeywordId, that.faqKeywordId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(faqId, faqKeywordId);
    }
}
