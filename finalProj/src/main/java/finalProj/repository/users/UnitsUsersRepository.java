package finalProj.repository.users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.users.Units;
import finalProj.domain.users.UnitsUsers;

@Repository
public interface UnitsUsersRepository extends JpaRepository<UnitsUsers, Integer> {
    // 找某位使用者最早入住的單位（戶），依 createdAt 排序
    UnitsUsers findTopByUser_UsersIdOrderByCreatedAtAsc(Integer usersId);

    List<UnitsUsers> findByUser_UsersId(Integer usersId);
}
