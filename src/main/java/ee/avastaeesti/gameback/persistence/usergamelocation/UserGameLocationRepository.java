package ee.avastaeesti.gameback.persistence.usergamelocation;

import ee.avastaeesti.gameback.persistence.randomgamelocation.RandomGameLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserGameLocationRepository extends JpaRepository<UserGameLocation, Integer> {


    @Query("select u from UserGameLocation u where u.userGame.id = :userGameId and u.state = :state")
    Optional<UserGameLocation> findNextLocationBy(Integer userGameId, String state);

    Optional<UserGameLocation> findFirstByUserGameIdAndStateOrderByIdAsc(Integer userGameId, String state);

    @Query("select u from UserGameLocation u where u.userGame.id = :userGameId and u.location.id = :locationId")
    Optional<UserGameLocation> findAnsweredLocationBy(Integer userGameId, Integer locationId);

    @Query("select u from UserGameLocation u where u.userGame.id = :userGameId")
    List<UserGameLocation> findGameBy(Integer userGameId);


}