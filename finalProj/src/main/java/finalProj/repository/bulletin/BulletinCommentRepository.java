package finalProj.repository.bulletin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.bulletin.BulletinComment;

@Repository
public interface BulletinCommentRepository extends JpaRepository<BulletinComment, Integer> {

}
