package finalProj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.users.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

}
