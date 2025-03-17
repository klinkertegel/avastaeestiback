package ee.avastaeesti.gameback.persistence.leaderboard;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LeaderBoardRepository extends JpaRepository<LeaderBoard, Integer> {
  @Query("select l from LeaderBoard l where l.game.id = :gameId")
  Optional<LeaderBoard> findUserScoreBy(Integer gameId);

/*  @Query("select l from LeaderBoard l where l.game.id = :gameId order by l.totalScore DESC")
  Optional<LeaderBoard> findTopScoreBy(Integer gameId, Pageable pageable);*/

  @Query("select l from LeaderBoard l where l.game.id = :gameId order by l.totalScore DESC")
  List<LeaderBoard> findTopScoreBy(Integer gameId, Pageable pageable);


  @Query("select l from LeaderBoard l where l.game.id = :gameId order by l.totalScore DESC")
  List<LeaderBoard> findTopScoresBy(Integer gameId, Pageable pageable);

  @Query("select l from LeaderBoard l where l.game.id = :gameId")
  Optional<LeaderBoard> findTimeBy(Integer gameId);


}