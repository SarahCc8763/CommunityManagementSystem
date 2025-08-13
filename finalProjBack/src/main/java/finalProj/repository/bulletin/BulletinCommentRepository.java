package finalProj.repository.bulletin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.bulletin.BulletinComment;

@Repository
public interface BulletinCommentRepository extends JpaRepository<BulletinComment, Integer> {
    public List<BulletinComment> findByParentComment_Id(Integer id);

}
