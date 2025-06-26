package finalProj.repository.users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.users.RolesUsers;
import finalProj.domain.users.Users;

@Repository
public interface RolesUsersRepository extends JpaRepository<RolesUsers, Integer> {
	List<RolesUsers> findByUser(Users user);
}
