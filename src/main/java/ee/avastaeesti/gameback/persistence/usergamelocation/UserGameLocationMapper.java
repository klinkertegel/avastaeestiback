package ee.avastaeesti.gameback.persistence.usergamelocation;

import ee.avastaeesti.gameback.controller.randomgame.dto.NextRandomLocation;
import ee.avastaeesti.gameback.util.BytesConverter;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserGameLocationMapper {

    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "location.name", target = "locationName")
    @Mapping(source = "location.longitude", target = "longitude")
    @Mapping(source = "location.latitude", target = "latitude")
    @Mapping(source = "location.clue", target = "clue")
    @Mapping(source = "location.imageData", target = "imageData", qualifiedByName = "toString")
    NextRandomLocation toNextRandomLocation(UserGameLocation randomGameLocation);

    @Named("toString")
    static String toString(byte[] imageData) {
        return BytesConverter.bytesArrayToString(imageData);
    }

}