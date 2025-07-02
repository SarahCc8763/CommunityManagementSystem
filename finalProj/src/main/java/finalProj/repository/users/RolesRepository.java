package finalProj.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.users.Roles;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {

}
