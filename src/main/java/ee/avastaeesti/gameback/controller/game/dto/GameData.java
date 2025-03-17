package ee.avastaeesti.gameback.controller.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link ee.avastaeesti.gameback.persistence.gamelocation.GameLocation}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameData implements Serializable {
    private Integer gameId;
    private List<Integer> locationIds;
}