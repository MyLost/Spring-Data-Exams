package softuni.exam.models.dto.json;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import softuni.exam.models.entity.TownEntity;

import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AgentDto {

    @Size(min = 1)
    @NotNull
    private String firstName;

    @Size(min = 1)
    @NotNull
    private String lastName;

    @Email
    @NotNull
    private String email;

    @NotNull
    @SerializedName("town")
    private String townName;
}
