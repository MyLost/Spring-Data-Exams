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

@Entity
@Table(name="towns")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TownEntity extends BaseEntity {

    @Size(min = 2)
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Positive
    private Integer population;

    @Column(columnDefinition = "text", nullable = false)
    private String guide;
}
