package finalProj.domain.bulletin;

import com.fasterxml.jackson.annotation.JsonBackReference;

import finalProj.domain.embed.BulletinCommentLikeId;
import finalProj.domain.users.Users;
import jakarta.persistence.*;

@Entity
@Table(name = "bulletin_comment_like")
public class BulletinCommentLike {

    @EmbeddedId
    private BulletinCommentLikeId id;

    @ManyToOne
    @MapsId("bulletinCommentId")
    @JoinColumn(name = "bulletin_comment_id")
    @JsonBackReference("bulletinComment-like")
    private BulletinComment comment;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonBackReference("bulletinCommentLike-user")
    private Users user;

    @Column(name = "bulletin_comment_like_is_valid", insertable = false)
    private Boolean isValid;

    public BulletinCommentLikeId getId() {
        return id;
    }

    public BulletinComment getComment() {
        return comment;
    }

    public void setComment(BulletinComment bulletinComment) {
        this.comment = bulletinComment;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setId(BulletinCommentLikeId id) {
        this.id = id;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    @Override
    public String toString() {
        return "BulletinCommentLike [id=" + id + ", bulletinComment=" + comment + ", user=" + user
                + ", isValid=" + isValid + "]";
    }

}
