package finalProj.repository.community;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.community.Community;

public interface CommunityRepository extends JpaRepository<Community, Integer> {
	
	List<Community> findByName(String name);
}
