package exam.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Entity
@Table(name="towns")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TownEntity extends BaseEntity {

    @Column(unique = true)
    @Size(min = 2)
    @NotNull
    private String name;

    @Positive
    private Integer population;

    @Size(min = 2)
    @Column(columnDefinition = "text", name="travel_guide")
    @NotNull
    private String travelGuide;
}
