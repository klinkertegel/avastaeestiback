package ee.avastaeesti.gameback.persistence.gamelocation;

import ee.avastaeesti.gameback.controller.gamelocation.dto.GameLocationInfo;
import ee.avastaeesti.gameback.util.BytesConverter;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface GameLocationMapper {
    @Mapping(source = "id", target = "gameLocationId")
    @Mapping(source = "location.name", target = "locationName")
    @Mapping(source = "location.imageData", target = "locationImage", qualifiedByName = "toString")
    GameLocationInfo toGameLocationInfo(GameLocation gameLocation);

    List<GameLocationInfo> toGameLocationInfos(List<GameLocation> gameLocations);

    @Named("toString")
    static String toString(byte[] imageData) {
        return BytesConverter.bytesArrayToString(imageData);
    }
}