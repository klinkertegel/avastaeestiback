package ee.avastaeesti.gameback.controller.location.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationInfo implements Serializable {
    private Integer locationId;
    @NotNull
    @Size(max = 255)
    private String locationName;
}