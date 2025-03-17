package ee.avastaeesti.gameback.service.gamelocation;

import ee.avastaeesti.gameback.controller.gamelocation.dto.GameLocationInfo;
import ee.avastaeesti.gameback.persistence.game.Game;
import ee.avastaeesti.gameback.persistence.game.GameRepository;
import ee.avastaeesti.gameback.persistence.gamelocation.GameLocation;
import ee.avastaeesti.gameback.persistence.gamelocation.GameLocationMapper;
import ee.avastaeesti.gameback.persistence.gamelocation.GameLocationRepository;
import ee.avastaeesti.gameback.persistence.location.Location;
import ee.avastaeesti.gameback.persistence.location.LocationRepository;
import ee.avastaeesti.gameback.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameLocationService {

    private final GameRepository gameRepository;
    private final LocationRepository locationRepository;
    private final GameLocationRepository gameLocationRepository;
    private final GameLocationMapper gameLocationMapper;

    public void addGameLocation(Integer gameId, Integer locationId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> ValidationService.throwForeignKeyNotFoundException("gameId", gameId));
        Location location = locationRepository.findById(locationId).orElseThrow(() -> ValidationService.throwForeignKeyNotFoundException("locationId", locationId));

        GameLocation gameLocation = new GameLocation();
        gameLocation.setGame(game);
        gameLocation.setLocation(location);
        gameLocationRepository.save(gameLocation);
    }

    public List<GameLocationInfo> getGameLocations(Integer gameId) {
        List<GameLocation> gameLocations = gameLocationRepository.findGameLocationsBy(gameId);
        List<GameLocationInfo> gameLocationInfos = gameLocationMapper.toGameLocationInfos(gameLocations);
        return gameLocationInfos;
    }

    public void removeLocationFromGameBy(Integer gameLocationId) {
        gameLocationRepository.deleteById(gameLocationId);
//        todo - siin setStatus punane ja ei tööta niipidi. ülemine lahendus töötab-
//        GameLocation gameLocation = gameLocationRepository.findById(gameLocationId).orElseThrow(() -> ValidationService.throwForeignKeyNotFoundException("gameLocationId", gameLocationId));
//        gameLocation.setStatus(DELETED.getCode());
//        gameLocationRepository.save(gameLocation);
    }
}
