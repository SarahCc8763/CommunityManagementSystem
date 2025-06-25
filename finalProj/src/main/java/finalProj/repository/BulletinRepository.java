package finalProj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.Bulletin;

public interface BulletinRepository extends JpaRepository<Bulletin, Integer> {

}
