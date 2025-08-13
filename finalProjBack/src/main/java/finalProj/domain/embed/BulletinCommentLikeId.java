package finalProj.domain.embed;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;

public class BulletinCommentLikeId implements Serializable {
    @Column(name = "bulletin_comment_id")
    private Integer bulletinCommentId;

    @Column(name = "user_id")
    private Integer userId;

    public BulletinCommentLikeId() {
    }

    public BulletinCommentLikeId(Integer bulletinCommentLikeId, Integer userId) {
        this.bulletinCommentId = bulletinCommentLikeId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof BulletinCommentLikeId))
            return false;
        BulletinCommentLikeId that = (BulletinCommentLikeId) o;
        return Objects.equals(bulletinCommentId, that.bulletinCommentId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bulletinCommentId, userId);
    }

    public Integer getBulletinCommentId() {
        return bulletinCommentId;
    }

    public void setBulletinCommentId(Integer bulletinCommentLikeId) {
        this.bulletinCommentId = bulletinCommentLikeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
