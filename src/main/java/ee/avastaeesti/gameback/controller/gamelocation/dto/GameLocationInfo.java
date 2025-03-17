package ee.avastaeesti.gameback.controller.gamelocation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameLocationInfo {
    private Integer gameLocationId;
    private String locationImage;
    private String locationName;
}
