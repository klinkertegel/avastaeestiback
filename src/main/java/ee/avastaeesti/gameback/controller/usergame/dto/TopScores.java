package ee.avastaeesti.gameback.controller.usergame.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopScores {
    private String userName;
    private Integer totalScore;
}

