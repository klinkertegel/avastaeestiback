package ee.avastaeesti.gameback.service.game;

import ee.avastaeesti.gameback.controller.game.dto.GameData;
import ee.avastaeesti.gameback.controller.game.dto.GameInfo;
import ee.avastaeesti.gameback.controller.game.dto.NewGame;
import ee.avastaeesti.gameback.controller.game.dto.UserGame;
import ee.avastaeesti.gameback.persistence.game.Game;
import ee.avastaeesti.gameback.persistence.game.GameMapper;
import ee.avastaeesti.gameback.persistence.game.GameRepository;
import ee.avastaeesti.gameback.persistence.gamelocation.GameLocation;
import ee.avastaeesti.gameback.persistence.gamelocation.GameLocationRepository;
import ee.avastaeesti.gameback.persistence.leaderboard.LeaderBoard;
import ee.avastaeesti.gameback.persistence.leaderboard.LeaderBoardRepository;
import ee.avastaeesti.gameback.persistence.location.Location;
import ee.avastaeesti.gameback.persistence.location.LocationRepository;
import ee.avastaeesti.gameback.persistence.user.User;
import ee.avastaeesti.gameback.persistence.user.UserRepository;
import ee.avastaeesti.gameback.status.Status;
import ee.avastaeesti.gameback.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static ee.avastaeesti.gameback.status.Status.DELETED;

@Service
@RequiredArgsConstructor
public class GameService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final LeaderBoardRepository leaderBoardRepository;
    private final GameLocationRepository gameLocationRepository;
    private final LocationRepository locationRepository;
    private final GameMapper gameMapper;

    public void saveGame(GameData gameData) {
        Game gameId = gameRepository.getReferenceById(gameData.getGameId());
        for (Integer locationId : gameData.getLocationIds()) {
            Location location = locationRepository.getReferenceById(locationId);

            GameLocation gameLocation = new GameLocation();
            gameLocation.setGame(gameId);
            gameLocation.setLocation(location);

            gameLocationRepository.save(gameLocation);
        }
    }

    public Integer createGame(NewGame newGame) {
        User user = userRepository.findById(newGame.getUserId())
                .orElseThrow(() -> ValidationService.throwForeignKeyNotFoundException("userId", newGame.getUserId()));
        Game game = gameMapper.toGame(newGame);
        game.setUser(user);
        gameRepository.save(game);
        return game.getId();
    }

    public List<GameInfo> getAllGames() {
        List<Game> games = gameRepository.findAllGamesBy(Status.ACTIVE.getCode());
        List<GameInfo> gameInfos = gameMapper.toGameInfos(games);
        addTotalScoreAndUsername(gameInfos);
        return gameInfos;
    }

    private void addTotalScoreAndUsername(List<GameInfo> gameInfos) {
        for (GameInfo gameInfo : gameInfos) {

            Pageable top1 = PageRequest.of(0, 1); // Leht 0, suurus 3
            List<LeaderBoard> optionalLeaderBoard = leaderBoardRepository.findTopScoreBy(gameInfo.getGameId(), top1);
            if (!optionalLeaderBoard.isEmpty()) {
                LeaderBoard leaderBoard = optionalLeaderBoard.get(0);
                gameInfo.setTotalTopScore(leaderBoard.getTotalScore());
                gameInfo.setUsername(leaderBoard.getUser().getUsername());
            } else {
                gameInfo.setTotalTopScore(0); // või mõni muu vaikimisi väärtus
            }
        }
    }

    public List<UserGame> getUserGames(Integer userId) {
        List<Game> games = gameRepository.findGamesBy(userId, Status.ACTIVE.getCode());
        List<UserGame> userGames = gameMapper.toUserGames(games);
        return userGames;
    }

    public void deleteUserGame(Integer gameId) {
        Game gameToBeRemoved = gameRepository.findById(gameId).orElseThrow(() -> ValidationService.throwPrimaryKeyNotFoundException("gameId", gameId));
        gameToBeRemoved.setStatus(DELETED.getCode());
        gameRepository.save(gameToBeRemoved);
    }
}
