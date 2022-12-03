package softuni.exam.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="planes")
public class PlaneEntity extends BaseEntity {

    @Column(name="register_number", nullable = false, unique = true)
    @Size(min = 5)
    private String registerNumber;

    @Positive
    @Column(nullable = false)
    private Integer capacity;

    @Size(min =2)
    @Column(nullable = false)
    private String airline;
}
