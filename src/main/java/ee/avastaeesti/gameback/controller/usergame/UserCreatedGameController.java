package ee.avastaeesti.gameback.controller.usergame;

import ee.avastaeesti.gameback.controller.randomgame.dto.GameOverResults;
import ee.avastaeesti.gameback.controller.randomgame.dto.NextRandomLocation;
import ee.avastaeesti.gameback.controller.randomgame.dto.RandomLocationAnswerResult;
import ee.avastaeesti.gameback.controller.randomgame.dto.UserAnswer;
import ee.avastaeesti.gameback.controller.usergame.dto.GameScoreResults;
import ee.avastaeesti.gameback.controller.usergame.dto.TopScores;
import ee.avastaeesti.gameback.service.usergame.UserCreatedGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
public class UserCreatedGameController {

    private final UserCreatedGameService userCreatedGameService;

    @GetMapping("/user/game/location")
    public NextRandomLocation getNextUserGameLocation(@RequestParam Integer userGameId) {
        NextRandomLocation nextUserGameLocation = userCreatedGameService.getNextUserGameLocation(userGameId);
        return nextUserGameLocation;
    }

    @PostMapping("user/game")
    public Integer createNewUserGame(@RequestParam Integer gameId, Integer userId) {
        Integer newUserGame = userCreatedGameService.createNewUserGame(gameId, userId);
        return newUserGame;
    }

    @PostMapping("/user/game/location/result")
    public RandomLocationAnswerResult getLocationResult(@RequestBody UserAnswer userAnswer) {
        return userCreatedGameService.getLocationResult(userAnswer);
    }

    @GetMapping("/user/game/gameover")
    public GameOverResults getGameOverResults(@RequestParam Integer userGameId) {
        GameOverResults gameOverResults = userCreatedGameService.getGameOverResults(userGameId);
        return gameOverResults;
    }

    @GetMapping("/user/game/score")
    public GameScoreResults getGameScoreResults(@RequestParam Integer userGameId) {
        GameScoreResults gameScoreResults = userCreatedGameService.getGameScoreResults(userGameId);
        return gameScoreResults;
    }

    @GetMapping("/user/game/topscores")
    public ArrayList<TopScores> getGameTopScores(@RequestParam Integer userGameId) {
        ArrayList<TopScores> gameTopScores = userCreatedGameService.getGameTopScores(userGameId);
        return gameTopScores;
    }


}

