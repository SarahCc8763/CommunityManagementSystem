package finalProj.repository.users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.users.Units;
import finalProj.domain.users.UnitsUsers;

@Repository
public interface UnitsUsersRepository extends JpaRepository<UnitsUsers, Integer> {
    // 找某位使用者最早入住的單位（戶），依 Id排序
    List<UnitsUsers> findByUnitOrderByUser_UsersIdAsc(Units unit);

    List<UnitsUsers> findByUser_UsersId(Integer usersId);

}
