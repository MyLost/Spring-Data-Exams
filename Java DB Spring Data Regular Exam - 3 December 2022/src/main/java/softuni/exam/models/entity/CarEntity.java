package softuni.exam.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.exam.models.enums.CarType;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="cars")
public class CarEntity extends BaseEntity {

    @Column(name="car_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CarType carType;

    @Column(name="car_make", nullable = false)
    @Size(min = 2, max = 30)
    private String carMake;

    @Column(name="car_model", nullable = false)
    @Size(min = 2, max = 30)
    private String carModel;

    @Positive
    @Column(nullable = false)
    private Integer year;

    @Column(name="plate_number", nullable = false, unique = true)
    @Size(min = 2, max = 30)
    private String plateNumber;

    @Positive
    @Column(nullable = false)
    private Integer kilometers;

    @Min(1)
    @Column(nullable = false)
    private Double engine;

    @OneToMany(mappedBy = "car", targetEntity = TaskEntity.class)
    private List<TaskEntity> tasks;
}
