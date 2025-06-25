package finalProj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.community.Community;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Integer> {

}
