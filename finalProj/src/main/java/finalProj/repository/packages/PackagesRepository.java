package finalProj.repository.packages;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import finalProj.domain.packages.Packages;

@Repository
public interface PackagesRepository extends JpaRepository<Packages, Integer> {

}
