package ee.avastaeesti.gameback.service.usergame;

import ee.avastaeesti.gameback.controller.randomgame.dto.GameOverResults;
import ee.avastaeesti.gameback.controller.randomgame.dto.NextRandomLocation;
import ee.avastaeesti.gameback.controller.randomgame.dto.RandomLocationAnswerResult;
import ee.avastaeesti.gameback.controller.randomgame.dto.UserAnswer;
import ee.avastaeesti.gameback.controller.usergame.dto.GameScoreResults;
import ee.avastaeesti.gameback.controller.usergame.dto.TopScores;
import ee.avastaeesti.gameback.infrastructure.Error;
import ee.avastaeesti.gameback.infrastructure.exception.DataNotFoundException;
import ee.avastaeesti.gameback.persistence.game.Game;
import ee.avastaeesti.gameback.persistence.game.GameRepository;
import ee.avastaeesti.gameback.persistence.gamelocation.GameLocation;
import ee.avastaeesti.gameback.persistence.gamelocation.GameLocationRepository;
import ee.avastaeesti.gameback.persistence.leaderboard.LeaderBoard;
import ee.avastaeesti.gameback.persistence.leaderboard.LeaderBoardRepository;
import ee.avastaeesti.gameback.persistence.location.Location;
import ee.avastaeesti.gameback.persistence.location.LocationMapper;
import ee.avastaeesti.gameback.persistence.location.LocationRepository;
import ee.avastaeesti.gameback.persistence.user.User;
import ee.avastaeesti.gameback.persistence.user.UserRepository;
import ee.avastaeesti.gameback.persistence.usergame.UserGame;
import ee.avastaeesti.gameback.persistence.usergame.UserGameRepository;
import ee.avastaeesti.gameback.persistence.usergamelocation.UserGameLocation;
import ee.avastaeesti.gameback.persistence.usergamelocation.UserGameLocationMapper;
import ee.avastaeesti.gameback.persistence.usergamelocation.UserGameLocationRepository;
import ee.avastaeesti.gameback.status.GameState;
import ee.avastaeesti.gameback.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCreatedGameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final UserGameRepository userGameRepository;
    private final LeaderBoardRepository leaderBoardRepository;
    private final GameLocationRepository gameLocationRepository;
    private final UserGameLocationRepository userGameLocationRepository;
    private final UserGameLocationMapper userGameLocationMapper;
    private final LocationMapper locationMapper;


    public Integer createNewUserGame(Integer gameId, Integer userId) {
        Game userCreatedGame = gameRepository.findById(gameId)
                .orElseThrow(() -> ValidationService.throwPrimaryKeyNotFoundException("gameId", gameId));


        User user = userRepository.findById(userId)
                .orElseThrow(() -> ValidationService.throwPrimaryKeyNotFoundException("userId", userId));

        long totalLocations = gameLocationRepository.countTotalLocationsBy(gameId);


        //TODO Loob uue randmon game tabeli
        UserGame userGame = new UserGame();
        userGame.setUser(user);
        userGame.setGame(userCreatedGame);
        userGame.setTotalLocations((int) totalLocations);
        userGame.setLocationsAnswered(0);
        userGame.setTotalScore(0);
        userGame.setCorrectAnswers(0);
        userGame.setIsComplete(false);

        userGameRepository.save(userGame);


        List<GameLocation> gameLocations = gameLocationRepository.findGameLocationsBy(gameId);
        ArrayList<UserGameLocation> userGameLocations = new ArrayList<>();

        for (GameLocation gameLocation : gameLocations) {
            UserGameLocation userGameLocation = new UserGameLocation();
            userGameLocation.setLocation(gameLocation.getLocation());
            userGameLocation.setGame(userCreatedGame);
            userGameLocation.setUserGame(userGame);
            userGameLocation.setUser(user);
            userGameLocation.setIsCorrect(false);
            if (userGameLocations.isEmpty()) {
                userGameLocation.setState(GameState.NEXT_LOCATION.getCode());
            } else {
                userGameLocation.setState(GameState.LOCATION_PENDING.getCode());
            }
            userGameLocations.add(userGameLocation);
        }

        userGameLocationRepository.saveAll(userGameLocations);

        //tagastame fronti random game gameId
        return userGame.getId();

    }

    public NextRandomLocation getNextUserGameLocation(Integer userGameId) {

        UserGame userGame = userGameRepository.findById(userGameId).orElseThrow();


        // Kindlusta, et refreshi vajutades ei tule uut rida, kui eelmine location on vastamata ehk tagasta fronti location, mille state on AP (answer pending)
        Optional<UserGameLocation> answerPendingUserGameLocation = userGameLocationRepository.findNextLocationBy(userGameId, GameState.ANSWER_PENDING.getCode());
        if (answerPendingUserGameLocation.isPresent()) {
            UserGameLocation userGameLocation = answerPendingUserGameLocation.get();
            NextRandomLocation nextUserGameLocation = userGameLocationMapper.toNextRandomLocation(userGameLocation);
            nextUserGameLocation.setIsGameComplete(userGame.getIsComplete());
            return nextUserGameLocation;
        }

        //Otsi järgmine location, mille state on NL (next location)
        UserGameLocation userGameNextLocation = userGameLocationRepository.findNextLocationBy(userGameId, GameState.NEXT_LOCATION.getCode())
                .orElseThrow(() -> new DataNotFoundException(Error.NO_RANDOM_LOCATION_FOUND.getMessage(), Error.NO_RANDOM_LOCATION_FOUND.getErrorCode()));


        //Muuda leitud location state AP (answer pending)
        userGameNextLocation.setState(GameState.ANSWER_PENDING.getCode());
        userGameLocationRepository.save(userGameNextLocation);

        //Otsi järgmine asukoht, mille state on LP (Location Pending), ja muuda selle state NL (next location)
        //LP puudub kui hangitakse viimast locationi

        Optional<UserGameLocation> locationPendingUserGameLocation = userGameLocationRepository
                .findFirstByUserGameIdAndStateOrderByIdAsc(userGameId, GameState.LOCATION_PENDING.getCode());
        if (locationPendingUserGameLocation.isPresent()) {
            UserGameLocation nextLocationPending = locationPendingUserGameLocation.get();
            nextLocationPending.setState(GameState.NEXT_LOCATION.getCode());
            userGameLocationRepository.save(nextLocationPending);
        }

        // Tagasta järgmise asukoha andmed
        NextRandomLocation nextUserGameLocation = locationMapper.toNextRandomLocation(userGameNextLocation.getLocation());
        nextUserGameLocation.setIsGameComplete(userGame.getIsComplete());

        return nextUserGameLocation;
    }

    public RandomLocationAnswerResult getLocationResult(UserAnswer userAnswer) {
        //Toon andmebaasist õiged location koordinaadid
        Location answeredLocation = locationRepository.getById(userAnswer.getLocationId());
        BigDecimal rightLongitude = answeredLocation.getLongitude();
        BigDecimal rightLatitude = answeredLocation.getLatitude();

        //Toon frondist kasutaja pandud koordinaadid
        BigDecimal userLongitude = userAnswer.getClickedLocation().getLng();
        BigDecimal userLatitude = userAnswer.getClickedLocation().getLat();

        //Kauguse arvutamine (Haversine valem)
        double distance = calculateDistance(rightLatitude.doubleValue(), rightLongitude.doubleValue(), userLatitude.doubleValue(), userLongitude.doubleValue());

        //Lubatud eksimuse määramine
        double allowedDistance = 10000; // meetrites

        //Kui kaugus on lubatust väiksem siis vastus on õige
        boolean answerIsCorrect = distance <= allowedDistance;

        // Uuenda andmebaasi tabelit random_game
        UserGame userGame = userGameRepository.findById(userAnswer.getRandomGameId())
                .orElseThrow(() -> new RuntimeException("Game not found"));
        userGame.setLocationsAnswered(userGame.getLocationsAnswered() + 1);

        if (userGame.getLocationsAnswered() + 1 >= userGame.getTotalLocations()) {
            userGame.setIsComplete(true);
        }
        // Salvesta uuendatud mängu andmed
        userGameRepository.save(userGame);

        //Uuenda andmebaasi tabelit random_game_location
        UserGameLocation userGameLocation = userGameLocationRepository
                .findAnsweredLocationBy(userAnswer.getRandomGameId(), userAnswer.getLocationId()).orElseThrow(() -> new RuntimeException("Location not found for randomGameId: " + userAnswer.getRandomGameId() + " and locationId: " + userAnswer.getLocationId()));
        userGameLocation.setState(GameState.LOCATION_ANSWERED.getCode());
        userGameLocation.setTimeStart(Instant.ofEpochMilli(userAnswer.getStartTimeMilliseconds()));
        userGameLocation.setTimeEnd(Instant.ofEpochMilli(userAnswer.getEndTimeMilliseconds()));
        if (answerIsCorrect) {
            userGameLocation.setIsCorrect(true);
        } else {
            userGameLocation.setIsCorrect(false);
        }

        //Salvesta uuendatud andmed (uus state)
        userGameLocationRepository.save(userGameLocation);


        // Loo ja tagasta DTO
        RandomLocationAnswerResult result = new RandomLocationAnswerResult();
        //todo: locatioId peab jõudma resultViewsse
        result.setLocationId(userAnswer.getLocationId());
        result.setLocationName(answeredLocation.getName());
        result.setGameIsComplete(userGame.getIsComplete());
        result.setTotalQuestions(userGame.getTotalLocations());
        result.setQuestionsAnswered(userGame.getLocationsAnswered());
        result.setLocationIsCorrect(userGameLocation.getIsCorrect());
/*
        if (answerIsCorrect) {
            result.setLocationIsCorrect(true);
        } else {
            result.setLocationIsCorrect(false);
        }*/

        return result;
    }

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Maapinna raadius kilomeetrites

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // Kaugus kilomeetrites
        return distance * 1000; // Tagastame meetrites
    }

    public GameOverResults getGameOverResults(Integer userGameId) {
        List<UserGameLocation> gameOverResults = userGameLocationRepository.findGameBy(userGameId);

        int correctCount = 0;
        int inCorrectCount = 0;
        long totalTime = 0;

        for (UserGameLocation gameOverResult : gameOverResults) {
            if (gameOverResult.getIsCorrect()) {
                correctCount++;
            } else {
                inCorrectCount++;
            }
            Duration duration = Duration.between(gameOverResult.getTimeStart(), gameOverResult.getTimeEnd());
            totalTime += duration.getSeconds();
        }

        return new GameOverResults(correctCount, inCorrectCount, totalTime);

    }

    @Transactional
    public GameScoreResults getGameScoreResults(Integer userGameId) {
        List<UserGameLocation> gameOverResults = userGameLocationRepository.findGameBy(userGameId);
        UserGame userGame = userGameRepository.findById(userGameId).orElseThrow();
        Game game = userGame.getGame();
        User user = userGame.getUser();

        int correctCount = 0;
        long totalTime = 0;

        for (UserGameLocation gameOverResult : gameOverResults) {
            if (gameOverResult.getIsCorrect()) {
                correctCount++;
                Duration duration = Duration.between(gameOverResult.getTimeStart(), gameOverResult.getTimeEnd());
                totalTime += duration.getSeconds();
            }
        }

        // Arvuta keskmine vastamise aeg õigete vastuste kohta
        double averageTimePerCorrectAnswer = correctCount > 0 ? (double) totalTime / correctCount : 0;

        // Arvuta skoor (näiteks: õigete vastuste arv * kaal / keskmine aeg)
        double score = calculateScore(correctCount, averageTimePerCorrectAnswer);

        // Teisenda skoor täisarvuks (ilma komakohata)
        int integerScore = (int) Math.round(score); // Ümardamine lähima täisarvuni

        // Teisenda totalTime (sekundites) Instant-iks
        Instant timestamp = Instant.ofEpochSecond(totalTime);

        //Salvesta andmebaasi tabelisse LeaderBoardDto
        LeaderBoard leaderBoard = new LeaderBoard();
        leaderBoard.setGame(game);
        leaderBoard.setUser(user);
        leaderBoard.setTimestamp(timestamp);
        leaderBoard.setTotalScore(integerScore);

        // Salvesta leaderBoard andmebaasi
        leaderBoardRepository.save(leaderBoard);

        return new GameScoreResults(integerScore);
    }

    // Näide skoori arvutamise meetodist
    private double calculateScore(int correctCount, double averageTimePerCorrectAnswer) {
        // Näiteks: skoor = (õigete vastuste arv * 100) / (keskmine aeg + 1)
        // Siin võid kasutada oma valemit, mis sobib sinu mängu loogikale
        return (correctCount * 100) / (averageTimePerCorrectAnswer + 1);


    }

    public ArrayList<TopScores> getGameTopScores(Integer userGameId) {
        UserGame userGame = userGameRepository.findById(userGameId).orElseThrow();
        Game game = userGame.getGame();
        String username = userGame.getUser().getUsername();


        Pageable top3 = PageRequest.of(0, 3); // Leht 0, suurus 3
        List<LeaderBoard> leaderBoards = leaderBoardRepository.findTopScoresBy(game.getId(), top3);
        ArrayList<TopScores> topScores = new ArrayList<>();

        for (LeaderBoard leaderBoard : leaderBoards) {

            TopScores gameTopScores = new TopScores();
            gameTopScores.setTotalScore(leaderBoard.getTotalScore());
          /*  gameTopScores.setUserName(username);*/
        gameTopScores.setUserName(leaderBoard.getUser().getUsername());

            topScores.add(gameTopScores);

        }

        return topScores;
    }
}

