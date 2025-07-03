package finalProj.repository.parking;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import finalProj.domain.community.Community;
import finalProj.domain.parking.ParkingType;

public interface ParkingTypeRepository extends JpaRepository<ParkingType, Integer> {

	boolean existsByTypeAndCommunity_CommunityId(String type, Integer communityId);

	List<ParkingType> findByCommunity_CommunityId(Integer communityId);

	Optional<ParkingType> findByTypeAndCommunity_CommunityId(String parkingType, Integer communityId);

	boolean existsByIdAndCommunity(Integer parkingTypeId, Community community);

}
