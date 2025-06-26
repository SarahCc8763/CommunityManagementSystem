
package finalProj.domain.bulletin;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import finalProj.domain.community.Community;
import finalProj.domain.poll.Poll;
import finalProj.domain.users.Users;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@DynamicInsert
@Table(name = "bulletin")
public class Bulletin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bulletin_id")
    private Integer id;

    @Column(name = "bulletin_title", nullable = false, length = 50)
    private String title;

    @Column(name = "bulletin_description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "bulletin_category")
    @JsonBackReference("bulletin-category")
    private BulletinCategory category;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("bulletin-user")
    private Users user;

    @Column(name = "bulletin_is_pinned")
    private Boolean isPinned;

    @Column(name = "bulletin_post_time")
    private LocalDateTime postTime;

    @Column(name = "bulletin_modify_time", insertable = false)
    private LocalDateTime modifyTime;

    @Column(name = "bulletin_remove_time")
    private LocalDateTime removeTime;

    @Column(name = "bulletin_post_status", insertable = false)
    private Boolean postStatus;

    @ManyToOne
    @JoinColumn(name = "community_id", nullable = true)
    @JsonBackReference("community-bulletin")
    private Community community;

    @OneToOne(mappedBy = "bulletin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("bulletin-poll")
    private Poll poll;

    @OneToMany(mappedBy = "bulletin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("bulletin-attachment")
    private List<BulletinAttachment> attachments;

    @OneToMany(mappedBy = "bulletin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("bulletin-comment")
    private List<BulletinComment> comments;

    public List<BulletinAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<BulletinAttachment> attachments) {
        this.attachments = attachments;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
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

    public BulletinCategory getCategory() {
        return category;
    }

    public void setCategory(BulletinCategory category) {
        this.category = category;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Boolean getIsPinned() {
        return isPinned;
    }

    public void setIsPinned(Boolean isPinned) {
        this.isPinned = isPinned;
    }

    public LocalDateTime getPostTime() {
        return postTime;
    }

    public void setPostTime(LocalDateTime postTime) {
        this.postTime = postTime;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }

    public LocalDateTime getRemoveTime() {
        return removeTime;
    }

    public void setRemoveTime(LocalDateTime removeTime) {
        this.removeTime = removeTime;
    }

    public Boolean getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(Boolean postStatus) {
        this.postStatus = postStatus;
    }

    @Override
    public String toString() {
        return "Bulletin [id=" + id + ", title=" + title + ", description=" + description + ", category=" + category
                + ", user=" + user + ", isPinned=" + isPinned + ", postTime=" + postTime + ", modifyTime=" + modifyTime
                + ", removeTime=" + removeTime + ", postStatus=" + postStatus + ", community=" + community + ", poll="
                + poll + ", attachments=" + attachments + ", comments=" + comments + "]";
    }

    public List<BulletinComment> getComments() {
        return comments;
    }

    public void setComments(List<BulletinComment> comments) {
        this.comments = comments;
    }
}
