package ee.avastaeesti.gameback.controller.game.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link ee.avastaeesti.gameback.persistence.game.Game}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewGame implements Serializable {
    private Integer userId;
    @NotNull
    @Size(max = 255)
    private String gameName;
    @NotNull
    @Size(max = 1000)
    private String description;
    @NotNull
    private Integer timePerLocation;
}