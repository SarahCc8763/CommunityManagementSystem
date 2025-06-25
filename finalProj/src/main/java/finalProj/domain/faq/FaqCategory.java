
package finalProj.domain.faq;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.*;

import finalProj.domain.community.Community;

import java.util.*;

@Entity
@Table(name = "faq_category")
public class FaqCategory {
    @Id
    @Column(name = "faq_category_name", length = 20)
    private String name;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference("faq-category")
    private List<Faq> faqs;

    @ManyToOne
    @JoinColumn(name = "community_id")
    @JsonBackReference("community-faq-category")
    private Community community;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Faq> getFaqs() {
        return faqs;
    }

    public void setFaqs(List<Faq> faqs) {
        this.faqs = faqs;
    }

    @Override
    public String toString() {
        return "FaqCategory [name=" + name + ", faqs=" + faqs + ", community=" + community + "]";
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }
}
