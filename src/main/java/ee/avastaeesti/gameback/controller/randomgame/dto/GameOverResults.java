package ee.avastaeesti.gameback.controller.randomgame.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameOverResults {
    private int correctCount;
    private int inCorrectCount;
    private long totalTime;
}
