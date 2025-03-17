package ee.avastaeesti.gameback.controller.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGame {
    private Integer gameId;
    private String gameName;
    private String gameDescription;
}
