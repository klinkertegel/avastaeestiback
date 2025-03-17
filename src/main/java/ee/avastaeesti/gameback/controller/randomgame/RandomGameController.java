package ee.avastaeesti.gameback.controller.randomgame;

import ee.avastaeesti.gameback.controller.randomgame.dto.GameOverResults;
import ee.avastaeesti.gameback.controller.randomgame.dto.NextRandomLocation;
import ee.avastaeesti.gameback.controller.randomgame.dto.RandomLocationAnswerResult;
import ee.avastaeesti.gameback.controller.randomgame.dto.UserAnswer;
import ee.avastaeesti.gameback.service.randomgame.RandomGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RandomGameController {

    private final RandomGameService randomGameService;

    @PostMapping("/random/game")
    public Integer createNewRandomGame(@RequestParam Integer userId) {
       return randomGameService.createNewRandomGame(userId);
    }

    @GetMapping("/random/game/location")
    public NextRandomLocation getNextRandomLocation(@RequestParam Integer randomGameId) {
       return randomGameService.getNextRandomLocation(randomGameId);
    }

    @PostMapping("/game/location/result")
    public RandomLocationAnswerResult getLocationResult(@RequestBody UserAnswer userAnswer) {
       return randomGameService.getLocationResult(userAnswer);
    }

    @GetMapping("/game/gameover")
    public GameOverResults getGameOverResults(@RequestParam Integer randomGameId) {
        GameOverResults gameOverResults = randomGameService.getGameOverResults(randomGameId);
        return gameOverResults;
    }

}
