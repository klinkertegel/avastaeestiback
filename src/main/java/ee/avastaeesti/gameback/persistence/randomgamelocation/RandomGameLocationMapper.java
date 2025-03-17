package ee.avastaeesti.gameback.persistence.randomgamelocation;

import ee.avastaeesti.gameback.controller.randomgame.dto.NextRandomLocation;
import ee.avastaeesti.gameback.controller.randomgame.dto.RandomLocationAnswerResult;
import ee.avastaeesti.gameback.controller.randomgame.dto.UserAnswer;
import ee.avastaeesti.gameback.util.BytesConverter;
import org.mapstruct.*;

import java.time.Instant;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RandomGameLocationMapper {

    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "location.name", target = "locationName")
    @Mapping(source = "location.longitude", target = "longitude")
    @Mapping(source = "location.latitude", target = "latitude")
    @Mapping(source = "location.clue", target = "clue")
    @Mapping(source = "location.imageData", target = "imageData", qualifiedByName = "toString")
    NextRandomLocation toNextRandomLocation(RandomGameLocation randomGameLocation);

    @Mapping(source = "startTimeMilliseconds", target = "timeStart", qualifiedByName = "millisecondsToInstant")
    @Mapping(source = "endTimeMilliseconds", target = "timeEnd", qualifiedByName = "millisecondsToInstant")
    RandomGameLocation toRandomGameLocation(UserAnswer userAnswer);

    @Named("toString")
    static String toString(byte[] imageData) {
        return BytesConverter.bytesArrayToString(imageData);
    }

    @Named("timestampToMilliseconds")
    static Long timestampToMilliseconds(Instant instant) {
        return instant == null ? null : instant.toEpochMilli(); }

    @Named("millisecondsToInstant")
    static Instant millisecondsToTimestamp(Long milliseconds) {
        return milliseconds == null ? null : Instant.ofEpochMilli(milliseconds);
    }

}