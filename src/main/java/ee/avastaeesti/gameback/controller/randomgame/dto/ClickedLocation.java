package ee.avastaeesti.gameback.controller.randomgame.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClickedLocation {
    @NotNull
    private BigDecimal lat;
    @NotNull
    private BigDecimal lng;
}
