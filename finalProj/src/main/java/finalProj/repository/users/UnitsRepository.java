package finalProj.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.users.Units;

@Repository
public interface UnitsRepository extends JpaRepository<Units, Integer> {

}
