package finalProj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.community.Community;
import finalProj.domain.users.Users;

public interface UsersRepository extends JpaRepository<Users, Integer> {

	List<Users> findByCommunity_CommunityId(Integer communityId);

	boolean existsByUsersIdAndCommunity(Integer usersId, Community community);

	Users findByName(String userName);

}
