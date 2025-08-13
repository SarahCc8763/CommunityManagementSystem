package finalProj.repository.bulletin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.bulletin.BulletinAttachment;

@Repository
public interface BulletinAttachmentRepository extends JpaRepository<BulletinAttachment, Integer> {

}
