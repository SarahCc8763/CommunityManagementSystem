package finalProj.repository.packages;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import finalProj.domain.packages.Packages;
import finalProj.domain.users.Units;

@Repository
public interface PackagesRepository extends JpaRepository<Packages, Integer> {
	List<Packages> findByUnit(Units unit);

	@Query("""
			    SELECT p FROM Packages p
			    WHERE (:status IS NULL OR p.status = :status)
			      AND (:type IS NULL OR p.type = :type)
			      AND (:arrivalTime IS NULL OR p.arrivalTime >= :arrivalTime)
			      AND p.unit.id = :unitId
			""")
	List<Packages> searchByConditions(
			@Param("status") String status,
			@Param("type") String type,
			@Param("arrivalTime") LocalDateTime arrivalTime,
			@Param("unitId") Integer unitId);

	@Modifying
	@Query("UPDATE Packages p SET p.status = :status, p.pickupTime = CURRENT_TIMESTAMP WHERE p.packagesId = :id")
	void updateStatusById(
			@Param("id") Integer id,
			@Param("status") String status);

}

// import finalProj.repository.packages.PackagesRepositroy;
