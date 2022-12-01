package softuni.exam.models.dto.json;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TownDto {

    @Size(min = 2)
    @NotNull
    private String townName;

    @Positive
    private int population;
}
