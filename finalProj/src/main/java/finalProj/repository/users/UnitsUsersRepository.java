package finalProj.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.users.UnitsUsers;

@Repository
public interface UnitsUsersRepository extends JpaRepository<UnitsUsers, Integer> {

}
