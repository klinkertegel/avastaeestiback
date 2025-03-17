package ee.avastaeesti.gameback.persistence.gamelocation;

import ee.avastaeesti.gameback.persistence.game.Game;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameLocationRepository extends JpaRepository<GameLocation, Integer> {
    @Query("select g from GameLocation g where g.game.id = :gameId order by g.id")
    List<GameLocation> findGameLocationsBy(Integer gameId);

    GameLocation game(@NotNull Game game);

    @Query("select count(g) from GameLocation g where g.game.id = :gameId")
    long countTotalLocationsBy(Integer gameId);
}