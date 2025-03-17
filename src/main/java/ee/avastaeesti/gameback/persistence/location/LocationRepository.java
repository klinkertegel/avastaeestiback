package ee.avastaeesti.gameback.persistence.location;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    @Query("select (count(l) > 0) from Location l where l.name = :locationName")
    boolean locationExistsBy(String locationName);

    @Query("SELECT l.clue FROM Location l WHERE l.id = :locationId")
    String findClueByLocationId(int locationId);

    @Query("SELECT l FROM Location l WHERE l.status = :status ORDER BY FUNCTION('RANDOM')")
    List<Location> findRandomLocationsBy(String status, Pageable pageable);

    @Query("select l from Location l where l.status = :status order by l.name")
    List<Location> findAllActiveLocationsBy(String status);
}