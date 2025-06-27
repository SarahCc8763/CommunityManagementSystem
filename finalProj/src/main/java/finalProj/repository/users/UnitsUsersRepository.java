package finalProj.repository.users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.users.Units;
import finalProj.domain.users.UnitsUsers;

@Repository
public interface UnitsUsersRepository extends JpaRepository<UnitsUsers, Integer> {
    List<UnitsUsers> findByUnitOrderByUser_UsersIdAsc(Units unit);
}
