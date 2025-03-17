package ee.avastaeesti.gameback.controller.location;

import ee.avastaeesti.gameback.controller.location.dto.LocationDto;
import ee.avastaeesti.gameback.controller.location.dto.LocationInfo;
import ee.avastaeesti.gameback.infrastructure.error.ApiError;
import ee.avastaeesti.gameback.service.location.LocationService;
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
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/locations")
    @Operation(summary = "Toob asukohtade nimed ja id-d listina (nt: ripploendi jaoks)")
    public List<LocationInfo> getLocations() {
        List<LocationInfo> locations = locationService.getLocations();
        return locations;
    }

    @GetMapping("/location")
    @Operation(summary = "Asukoha info äratoomine locationId abil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Ei leidnud primary keyd (errorCode 115)", content = @Content(schema = @Schema(implementation = ApiError.class))),
    })
    public LocationDto getLocation(@RequestParam Integer locationId) {
        LocationDto location = locationService.getLocation(locationId);
        return location;
    }

    @PutMapping("/location")
    @Operation(summary = "Asukoha info muutmine locationId abil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Ei leidnud primary keyd (errorCode 115)", content = @Content(schema = @Schema(implementation = ApiError.class))),
    })
    public void updateLocation(@RequestParam Integer locationId, @RequestBody LocationDto locationDto) {
        locationService.updateLocation(locationId, locationDto);
    }

    @PostMapping("/location")
    @Operation(summary = "Uue asukoha lisamine.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Sellise nimega asukoht on juba süsteemis olemas", content = @Content(schema = @Schema(implementation = ApiError.class))),
    })
    public void addLocation(@RequestBody LocationDto locationDto) {
        locationService.addLocation(locationDto);
    }

    @DeleteMapping("/home-admin")
    @Operation(summary = "Asukoha eemaldamine nimekirjast, andmebaasis muudetakse status A=>D")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Ei leidnud primary keyd (errorcode 115)", content = @Content(schema = @Schema(implementation = ApiError.class))),
    })
    public void removeLocation(@RequestParam Integer locationId) {
        locationService.removeLocation(locationId);
    }
}
