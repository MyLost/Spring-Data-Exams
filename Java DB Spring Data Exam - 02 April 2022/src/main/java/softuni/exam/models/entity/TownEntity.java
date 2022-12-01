package softuni.exam.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name="towns")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TownEntity extends BaseEntity {

    @Column(unique = true, name="town_name")
    @Size(min = 1)
    @NotNull
    private String townName;

    @Column
    @Positive
    @NotNull
    private int population;

    @OneToMany(mappedBy = "town", targetEntity = AgentEntity.class)
    private List<AgentEntity> agents;

    @OneToMany(mappedBy = "town", targetEntity = ApartmentEntity.class)
    private List<ApartmentEntity> apartments;
}
