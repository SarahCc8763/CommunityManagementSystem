package finalProj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.community.Community;

public interface CommunityRepository extends JpaRepository<Community, Integer> {

	Community findByCommunityId(Integer communityId);
}
