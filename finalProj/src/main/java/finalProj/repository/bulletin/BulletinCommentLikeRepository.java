package finalProj.repository.bulletin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.bulletin.BulletinCommentLike;
import finalProj.domain.embed.BulletinCommentLikeId;

@Repository
public interface BulletinCommentLikeRepository extends JpaRepository<BulletinCommentLike, BulletinCommentLikeId> {

}
