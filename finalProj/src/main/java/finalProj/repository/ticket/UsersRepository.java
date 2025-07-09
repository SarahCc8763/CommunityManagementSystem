package finalProj.repository.ticket;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.users.Users;

public interface UsersRepository extends JpaRepository<Users, Integer>{

}
