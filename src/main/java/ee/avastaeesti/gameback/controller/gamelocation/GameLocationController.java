package ee.avastaeesti.gameback.controller.gamelocation;

import ee.avastaeesti.gameback.controller.gamelocation.dto.GameLocationInfo;
import ee.avastaeesti.gameback.infrastructure.error.ApiError;
import ee.avastaeesti.gameback.service.gamelocation.GameLocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GameLocationController {

    private final GameLocationService gameLocationService;

    @GetMapping("/game-locations")
    @Operation(summary = "Tagastab kasutajamängu asukohad gameId abil")
    public List<GameLocationInfo> getGameLocations(@RequestParam Integer gameId) {
        return gameLocationService.getGameLocations(gameId);
    }

    @PostMapping("/game-location")
    @Operation(summary = "Kasutajamängule uue asukoha lisamine gameId ja locationId abil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Ei leidnud primary keyd (errorCode 115)", content = @Content(schema = @Schema(implementation = ApiError.class))),
    })
    public void addGameLocation(@RequestParam Integer gameId, @RequestParam Integer locationId) {
        gameLocationService.addGameLocation(gameId, locationId);
    }

    @DeleteMapping("/game-location")
    @Operation(summary = "Eemaldab kasutajamängust asukoha gameLocationId abil")
    public void deleteGameLocation(@RequestParam Integer gameLocationId) {
        gameLocationService.removeLocationFromGameBy(gameLocationId);
    }
}
