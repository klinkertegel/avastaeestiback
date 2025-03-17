package ee.avastaeesti.gameback.persistence.favorite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Integer> {

    @Query("SELECT f FROM Favourite f WHERE f.user.id = :userId AND f.status = :status")
    List<Favourite> findFavouritesBy(Integer userId, String status);

    @Query("select (count(f) > 0) from Favourite f where f.user.id = :userId and f.location.id = :locationId")
    boolean existsByUserIdAndLocationId(Integer userId, Integer locationId);


}

