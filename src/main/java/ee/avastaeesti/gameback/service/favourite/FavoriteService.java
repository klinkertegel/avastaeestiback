package ee.avastaeesti.gameback.service.favourite;

import ee.avastaeesti.gameback.controller.favourite.dto.UserFavourite;
import ee.avastaeesti.gameback.infrastructure.Error;
import ee.avastaeesti.gameback.infrastructure.exception.ForbiddenException;
import ee.avastaeesti.gameback.persistence.favorite.Favourite;
import ee.avastaeesti.gameback.persistence.favorite.FavouriteRepository;
import ee.avastaeesti.gameback.persistence.location.Location;
import ee.avastaeesti.gameback.persistence.location.LocationRepository;
import ee.avastaeesti.gameback.persistence.user.User;
import ee.avastaeesti.gameback.persistence.user.UserRepository;
import ee.avastaeesti.gameback.status.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static ee.avastaeesti.gameback.infrastructure.Error.ALREADY_FAVORITE;

@Service
@RequiredArgsConstructor
public class FavoriteService {


    private final FavouriteRepository favouriteRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    public List<UserFavourite> getUserFavourites(Integer userId) {
        List<Favourite> favourites = favouriteRepository.findFavouritesBy(userId, Status.ACTIVE.getCode());

        Integer indexCounter = 1;
        List<UserFavourite> userFavourites = new ArrayList<>();

        for (Favourite favourite : favourites) {
            UserFavourite userFavourite = new UserFavourite();
            userFavourite.setIndex(indexCounter);
            userFavourite.setLocationId(favourite.getLocation().getId());
            indexCounter++;
            userFavourites.add(userFavourite);
        }

        return userFavourites;
    }

    public void addUserFavorites(Integer userId, Integer locationId) {
        // Kontrolli, kas lemmik on juba olemas
        boolean isAlreadyFavourite = favouriteRepository.existsByUserIdAndLocationId(userId, locationId);

        if (isAlreadyFavourite) {
            throw new ForbiddenException(ALREADY_FAVORITE.getMessage(), ALREADY_FAVORITE.getErrorCode());
        }

        User user = userRepository.findById(userId).orElseThrow();
        Location location = locationRepository.findById(locationId).orElseThrow();

        Favourite favourite = new Favourite();
        favourite.setUser(user);
        favourite.setLocation(location);
        favourite.setStatus(Status.ACTIVE.getCode());

        favouriteRepository.save(favourite);
    }
}
