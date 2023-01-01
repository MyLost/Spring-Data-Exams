package softuni.exam.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="parts")
public class PartEntity extends BaseEntity {

    @Column(name="part_name", nullable = false, unique = true)
    @Size(min = 2, max = 19)
    private String partName;

    @Min(10)
    @Max(2000)
    @Column(nullable = false)
    private Double price;

    @Positive
    @Column(nullable = false)
    private Integer quantity;

    @OneToMany(mappedBy = "part", targetEntity = TaskEntity.class)
    private List<TaskEntity> tasks;
}
