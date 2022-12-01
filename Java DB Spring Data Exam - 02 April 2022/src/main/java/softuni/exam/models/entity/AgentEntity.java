package softuni.exam.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name="agents")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgentEntity extends  BaseEntity {

    @Column(name="first_name", unique = true)
    @Size(min = 1)
    @NotNull
    private String firstName;

    @Column(name="last_name")
    @Size(min = 1)
    @NotNull
    private String lastName;

    @Email
    @Column(unique = true)
    @NotNull
    private String email;

    @OneToOne
    private TownEntity town;

    @OneToMany(mappedBy = "agent", targetEntity = OfferEntity.class)
    private List<OfferEntity> offers;
}
