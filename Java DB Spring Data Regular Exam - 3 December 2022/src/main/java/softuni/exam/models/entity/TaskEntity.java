package softuni.exam.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.exam.models.enums.CarType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tasks")
public class TaskEntity extends BaseEntity {

    @Column(precision = 19, scale = 2, nullable = false)
    @Positive
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDateTime date;

    @OneToOne
    private MechanicEntity mechanic;

    @OneToOne
    private PartEntity part;

    @OneToOne
    private CarEntity car;

    @Override
    public String toString() {
        return String.format("• Car %s %s with %dkm\n" +
                "-Mechanic: %s %s - task №%d:\n" +
                "--Engine: %.1f\n" +
                "---Price: %.2f$", car.getCarMake(), car.getCarModel(), car.getKilometers(), mechanic.getFirstName(),
                mechanic.getLastName(), getId(), car.getEngine(), getPrice());
    }
}
