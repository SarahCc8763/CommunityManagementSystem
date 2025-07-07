package finalProj.repository.users;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.community.Community;
import finalProj.domain.users.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
	Optional<Users> findByEmail(String email);

	List<Users> findByCommunity_CommunityId(Integer communityId);

	boolean existsByUsersIdAndCommunity(Integer usersId, Community community);

	Users findByName(String userName);

	Users findByUsersId(Integer usersId);
}
