package ee.avastaeesti.gameback.service.randomgame;

import ee.avastaeesti.gameback.controller.randomgame.dto.GameOverResults;
import ee.avastaeesti.gameback.controller.randomgame.dto.NextRandomLocation;
import ee.avastaeesti.gameback.controller.randomgame.dto.RandomLocationAnswerResult;
import ee.avastaeesti.gameback.controller.randomgame.dto.UserAnswer;
import ee.avastaeesti.gameback.infrastructure.Error;
import ee.avastaeesti.gameback.infrastructure.exception.DataNotFoundException;
import ee.avastaeesti.gameback.persistence.location.Location;
import ee.avastaeesti.gameback.persistence.location.LocationMapper;
import ee.avastaeesti.gameback.persistence.location.LocationRepository;
import ee.avastaeesti.gameback.persistence.randomgame.RandomGame;
import ee.avastaeesti.gameback.persistence.randomgame.RandomGameRepository;
import ee.avastaeesti.gameback.persistence.randomgamelocation.RandomGameLocation;
import ee.avastaeesti.gameback.persistence.randomgamelocation.RandomGameLocationMapper;
import ee.avastaeesti.gameback.persistence.randomgamelocation.RandomGameLocationRepository;
import ee.avastaeesti.gameback.persistence.user.User;
import ee.avastaeesti.gameback.persistence.user.UserRepository;
import ee.avastaeesti.gameback.status.GameState;
import ee.avastaeesti.gameback.status.Status;
import ee.avastaeesti.gameback.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RandomGameService {


    private final UserRepository userRepository;
    private final RandomGameRepository randomGameRepository;
    private final LocationRepository locationRepository;
    private final RandomGameLocationRepository randomGameLocationRepository;
    private final LocationMapper locationMapper;
    private final RandomGameLocationMapper randomGameLocationMapper;

    public Integer createNewRandomGame(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ValidationService.throwPrimaryKeyNotFoundException("userId", userId));
// Loob uue randmon game tabeli
        RandomGame randomGame = new RandomGame();
        randomGame.setUser(user);
        randomGame.setTotalLocations(5);
        randomGame.setLocationsAnswered(0);
        randomGame.setIsComplete(false);

        randomGameRepository.save(randomGame);
//lisame random gameile random locations
        List<Location> randomLocations = locationRepository
                .findRandomLocationsBy(Status.ACTIVE.getCode(), PageRequest.of(0, 5));
        List<RandomGameLocation> randomGameLocations = new ArrayList<>();

        for (Location randomLocation : randomLocations) {

            RandomGameLocation randomGameLocation = new RandomGameLocation();
            randomGameLocation.setRandomGame(randomGame);
            randomGameLocation.setLocation(randomLocation);
            randomGameLocation.setIsCorrect(false);
//määrame ära mängualguse iga locationi State (NL, LP)
            if (randomGameLocations.isEmpty()) {
                randomGameLocation.setState(GameState.NEXT_LOCATION.getCode());
            } else {
                randomGameLocation.setState(GameState.LOCATION_PENDING.getCode());
            }
            randomGameLocations.add(randomGameLocation);
        }

        randomGameLocationRepository.saveAll(randomGameLocations);
//tagastame fronti random game gameId
        return randomGame.getId();

    }

    public NextRandomLocation getNextRandomLocation(Integer randomGameId) {
        RandomGame randomGame = randomGameRepository.findById(randomGameId)
                .orElseThrow(() -> ValidationService.throwPrimaryKeyNotFoundException("randomGameId", randomGameId));


        // Kindlusta, et refreshi vajutades ei tule uut rida, kui eelmine location on vastamata ehk tagasta fronti location, mille state on AP (answer pending)
        Optional<RandomGameLocation> answerPendingRandomGameLocation = randomGameLocationRepository
                .findRandomGameLocationBy(randomGameId, GameState.ANSWER_PENDING.getCode());
        if (answerPendingRandomGameLocation.isPresent()) {
            RandomGameLocation randomGameLocation = answerPendingRandomGameLocation.get();
            NextRandomLocation nextRandomLocation = randomGameLocationMapper.toNextRandomLocation(randomGameLocation);
            nextRandomLocation.setIsGameComplete(randomGame.getIsComplete());
            return nextRandomLocation;
        }

        //Otsi järgmine location, mille state on NL (next location)
        RandomGameLocation randomGameLocation = randomGameLocationRepository.findRandomGameLocationBy(randomGameId, GameState.NEXT_LOCATION.getCode())
                .orElseThrow(() -> new DataNotFoundException(Error.NO_RANDOM_LOCATION_FOUND.getMessage(), Error.NO_RANDOM_LOCATION_FOUND.getErrorCode()));

        //Muuda leitud location state AP (answer pending)
        randomGameLocation.setState(GameState.ANSWER_PENDING.getCode());
        randomGameLocationRepository.save(randomGameLocation);

        //Otsi järgmine asukoht, mille state on LP (Location Pending), ja muuda selle state NL (next location)
        //LP puudub kui hangitakse viimast locationi

        Optional<RandomGameLocation> locationPendingRandomGameLocation = randomGameLocationRepository
                .findFirstByRandomGameIdAndStateOrderByIdAsc(randomGameId, GameState.LOCATION_PENDING.getCode());
        if (locationPendingRandomGameLocation.isPresent()) {
            RandomGameLocation nextLocationPending = locationPendingRandomGameLocation.get();
            nextLocationPending.setState(GameState.NEXT_LOCATION.getCode());
            randomGameLocationRepository.save(nextLocationPending);
        }

        // Tagasta järgmise asukoha andmed
        NextRandomLocation nextRandomLocation = locationMapper.toNextRandomLocation(randomGameLocation.getLocation());
        nextRandomLocation.setIsGameComplete(randomGame.getIsComplete());

        return nextRandomLocation;
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
        RandomGame randomGame = randomGameRepository.findById(userAnswer.getRandomGameId())
                .orElseThrow(() -> new RuntimeException("Game not found"));
        randomGame.setLocationsAnswered(randomGame.getLocationsAnswered() + 1);

        if (randomGame.getLocationsAnswered() +1 >= randomGame.getTotalLocations()) {
            randomGame.setIsComplete(true);
        }
        // Salvesta uuendatud mängu andmed
        randomGameRepository.save(randomGame);

        //Uuenda andmebaasi tabelit random_game_location
        RandomGameLocation randomGameLocation = randomGameLocationRepository.findAnsweredLocationBy(userAnswer.getRandomGameId(), userAnswer.getLocationId()).orElseThrow(() -> new RuntimeException("Location not found for randomGameId: " + userAnswer.getRandomGameId() + " and locationId: " + userAnswer.getLocationId()));
        randomGameLocation.setState(GameState.LOCATION_ANSWERED.getCode());
        randomGameLocation.setTimeStart(Instant.ofEpochMilli(userAnswer.getStartTimeMilliseconds()));
        randomGameLocation.setTimeEnd(Instant.ofEpochMilli(userAnswer.getEndTimeMilliseconds()));
        if (answerIsCorrect) {
            randomGameLocation.setIsCorrect(true);
        } else {
            randomGameLocation.setIsCorrect(false);
        }

        //Salvesta uuendatud andmed (uus state)
        randomGameLocationRepository.save(randomGameLocation);


        // Loo ja tagasta DTO
        RandomLocationAnswerResult result = new RandomLocationAnswerResult();
        //todo: locatioId peab jõudma resultViewsse
        result.setLocationId(userAnswer.getLocationId());
        result.setLocationName(answeredLocation.getName());
        result.setGameIsComplete(randomGame.getIsComplete());
        result.setTotalQuestions(randomGame.getTotalLocations());
        result.setQuestionsAnswered(randomGame.getLocationsAnswered());
        result.setLocationIsCorrect(randomGameLocation.getIsCorrect());
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


    public GameOverResults getGameOverResults(Integer randomGameId) {
        List<RandomGameLocation> gameOverResults = randomGameLocationRepository.findGameBy(randomGameId);

        int correctCount = 0;
        int inCorrectCount = 0;
        long totalTime = 0;

        for (RandomGameLocation gameOverResult : gameOverResults) {
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
}



