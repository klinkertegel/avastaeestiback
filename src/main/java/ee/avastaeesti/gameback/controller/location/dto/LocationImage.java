package ee.avastaeesti.gameback.controller.location.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link ee.avastaeesti.gameback.persistence.location.Location}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationImage implements Serializable {
    private Integer locationId;
    @NotNull
    @Size(max = 255)
    private String locationName;
    @NotNull
    private String imageData;
}