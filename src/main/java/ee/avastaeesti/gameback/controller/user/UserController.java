package ee.avastaeesti.gameback.controller.user;

import ee.avastaeesti.gameback.controller.user.dto.UserDto;
import ee.avastaeesti.gameback.infrastructure.error.ApiError;
import ee.avastaeesti.gameback.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user")
    @Operation(summary = "toob ära kasutaja andmed UserId abil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Ei leidnud primary keyd (errorCode 115)", content = @Content(schema = @Schema(implementation = ApiError.class))),
    })
    public UserDto getUser(@RequestParam Integer userId) {
        UserDto userDto = userService.getUser(userId);
        return userDto;
    }

    @PostMapping("/user")
    @Operation(summary = "Uue kasutaja loomine")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Kasutaja loomine ebaõnnestus: "
                    + "Sellise nimega kasutaja on juba olemas (errorCode 112) või "
                    + "sellise e-mailiga kasutaja on juba süsteemis olemas (errorCode 113)",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
    })
    public void addNewUser(@RequestBody UserDto userDto) {
        userService.addNewUser(userDto);
    }

    @PutMapping("/user")
    @Operation(summary = "Kasutaja info muutmine userId abil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Kasutaja loomine ebaõnnestus: "
                    + "Sellise nimega kasutaja on juba olemas (errorCode 112) või "
                    + "sellise e-mailiga kasutaja on juba süsteemis olemas (errorCode 113)",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Ei leidnud primary keyd (errorCode 115)", content = @Content(schema = @Schema(implementation = ApiError.class))),
    })
    public void updateUser(@RequestParam Integer userId, @RequestBody UserDto userDto) {
        userService.updateUser(userId, userDto);
    }

    @DeleteMapping("/user")
    @Operation(summary = "Kasutaja info eemaldamine userId abil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Ei leidnud primary keyd (errorCode 115)", content = @Content(schema = @Schema(implementation = ApiError.class))),
    })
    public void removeUser(@RequestParam Integer userId) {
        userService.removeUser(userId);
    }
}
