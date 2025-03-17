package ee.avastaeesti.gameback.controller.favourite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFavourite {
    private Integer index;
    private Integer locationId;
}
