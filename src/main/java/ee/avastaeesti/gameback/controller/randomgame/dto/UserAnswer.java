package ee.avastaeesti.gameback.controller.randomgame.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAnswer {
    private Integer randomGameId;
    private Integer locationId;
    private ClickedLocation clickedLocation;
    private Long startTimeMilliseconds;
    private Long endTimeMilliseconds;

}
