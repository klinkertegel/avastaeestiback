package ee.avastaeesti.gameback.controller.favourite;

import ee.avastaeesti.gameback.controller.favourite.dto.UserFavourite;
import ee.avastaeesti.gameback.service.favourite.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;


    @GetMapping("/user-favourites")
    @Operation(summary = "Toob kasutaja lemmikasukohad koos piltidega userid abil")
    public List<UserFavourite> getUserFavourites(@RequestParam Integer userId) {
        List<UserFavourite> userFavourites = favoriteService.getUserFavourites(userId);
        return userFavourites;
    }

    @PostMapping("/add-favorites")
    public void addUserFavorites(@RequestParam Integer userId, @RequestParam Integer locationId) {
        favoriteService.addUserFavorites(userId, locationId);
    }



}
