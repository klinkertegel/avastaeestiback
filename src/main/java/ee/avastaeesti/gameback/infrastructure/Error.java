package ee.avastaeesti.gameback.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Error {
    INCORRECT_CREDENTIALS("Vale kasutajanimi või parool", 111),
    USERNAME_EXISTS("Sellise nimega kasutaja on juba olamas", 112),
    EMAIL_EXISTS("Sellise e-mailiga kasutaja on juba süsteemis olemas", 113),
    LOCATION_EXISTS("Sellise nimega asukoht on juba süsteemis olemas", 114),

    PRIMARY_KEY_NOT_FOUND("Ei leidnud primary keyd: ", 115),
    FOREIGN_KEY_NOT_FOUND("Ei leidnud foreign keyd: ", 116),

    USER_SCORE_NOT_FOUND("Ei leidnud userScore'i", 117),
    TOP_SCORE_NOT_FOUND("Ei leidnud topScore'i", 118),
    NO_RANDOM_LOCATION_FOUND("Ei leidnud ühtegi järgmist juhuslikku asukohta", 119),
    ALREADY_FAVORITE("See asukoht on juba Sinu lemmikute seas", 220);

    private final String message;
    private final int errorCode;
}
