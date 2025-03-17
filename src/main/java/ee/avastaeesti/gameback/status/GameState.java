package ee.avastaeesti.gameback.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum GameState {
    NEXT_LOCATION("NL"),
    ANSWER_PENDING("AP"),
    LOCATION_PENDING("LP"),
    LOCATION_ANSWERED("XX");

    private final String code;
}
