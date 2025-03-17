package ee.avastaeesti.gameback.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Status {
    ACTIVE("A"),
    DELETED("D");

    private final String code;

}
