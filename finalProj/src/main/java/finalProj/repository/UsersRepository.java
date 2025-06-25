package finalProj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.Users;

public interface UsersRepository extends JpaRepository<Users, Integer> {

}
