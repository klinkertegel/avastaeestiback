package ee.avastaeesti.gameback.controller.randomgame.dto;

import ee.avastaeesti.gameback.controller.location.dto.LocationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class NextRandomLocation extends LocationDto {
    private Boolean isGameComplete;

}
