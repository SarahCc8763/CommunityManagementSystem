
package finalProj.domain.bulletin;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import finalProj.domain.community.Community;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "bulletin_category")
public class BulletinCategory {
    @Id
    @Column(name = "bulletin_category_name", length = 20)
    private String name;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference("bulletin-category")
    private List<Bulletin> bulletins;

    @ManyToOne
    @JoinColumn(name = "community_id", nullable = true)
    @JsonBackReference("community-bulletin-category")
    private Community community;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Bulletin> getBulletins() {
        return bulletins;
    }

    public void setBulletins(List<Bulletin> bulletins) {
        this.bulletins = bulletins;
    }

    @Override
    public String toString() {
        return "BulletinCategory [name=" + name + ", bulletins=" + bulletins + ", community=" + community + "]";
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }
}
