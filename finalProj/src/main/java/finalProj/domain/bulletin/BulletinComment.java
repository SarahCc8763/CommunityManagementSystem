
package finalProj.domain.bulletin;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import finalProj.domain.users.Users;
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
@Table(name = "bulletin_comment")
public class BulletinComment {
    //
    // 屬性
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bulletin_comment_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "bulletin_id")
    @JsonBackReference("bulletin-comment")
    private Bulletin bulletin;

    @Column(name = "bulletin_comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("bulletinComment-user")
    private Users user;

    @Column(name = "bulletin_comment_time", insertable = false)
    private LocalDateTime time;

    @Column(name = "bulletin_comment_is_alive", insertable = false)
    private Boolean isAlive;

    @OneToMany(mappedBy = "comment")
    @JsonManagedReference("bulletinComment-like")
    private List<BulletinCommentLike> bulletinCommentLikes;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    @JsonBackReference("bulletinComment-parent")
    private BulletinComment parentComment;

    @OneToMany(mappedBy = "parentComment")
    @JsonManagedReference("bulletinComment-parent")
    private List<BulletinComment> replies;

    @Transient
    private Long likeCount;

    //
    //
    // getters and setters

    public Integer getId() {
        return id;
    }

    public BulletinComment getParentComment() {
        return parentComment;
    }

    public void setParentComment(BulletinComment parentComment) {
        this.parentComment = parentComment;
    }

    public List<BulletinComment> getReplies() {
        return replies;
    }

    public void setReplies(List<BulletinComment> replies) {
        this.replies = replies;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Bulletin getBulletin() {
        return bulletin;
    }

    public void setBulletin(Bulletin bulletin) {
        this.bulletin = bulletin;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Boolean getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(Boolean isAlive) {
        this.isAlive = isAlive;
    }

    @Override
    public String toString() {
        return "BulletinComment [id=" + id + ", bulletin=" + bulletin + ", comment=" + comment + ", user=" + user
                + ", time=" + time + ", isAlive=" + isAlive + ", bulletinCommentLikes=" + bulletinCommentLikes + "]";
    }

    public List<BulletinCommentLike> getBulletinCommentLikes() {
        return bulletinCommentLikes;
    }

    public void setBulletinCommentLikes(List<BulletinCommentLike> bulletinCommentLikes) {
        this.bulletinCommentLikes = bulletinCommentLikes;
    }

    public Long getLikeCount() {
        if (bulletinCommentLikes == null)
            return 0L;
        return (long) bulletinCommentLikes.size();
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }
}
