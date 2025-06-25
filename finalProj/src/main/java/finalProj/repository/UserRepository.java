package finalProj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
