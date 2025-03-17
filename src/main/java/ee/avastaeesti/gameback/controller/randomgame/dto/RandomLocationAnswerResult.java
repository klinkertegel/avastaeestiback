package ee.avastaeesti.gameback.controller.randomgame.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RandomLocationAnswerResult {
    private Integer locationId;
    private String locationName;
    private Boolean locationIsCorrect;
    private Boolean gameIsComplete;
    private Integer totalQuestions;
    private Integer questionsAnswered;


}
