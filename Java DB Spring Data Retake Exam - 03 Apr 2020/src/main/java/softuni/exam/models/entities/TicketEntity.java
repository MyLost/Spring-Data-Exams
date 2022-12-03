package softuni.exam.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="tickets")
public class TicketEntity extends BaseEntity {

    @Column(name="serial_number", nullable = false, unique = true)
    @Size(min = 2)
    private String serialNumber;

    @Column(nullable = false)
    @Positive
    private BigDecimal price;

    @Column(name="take_off", nullable = false)
    private LocalDateTime takeoff;

    @ManyToOne
    @JoinColumn(name = "from_town_id")
    private TownEntity fromTown;

    @ManyToOne
    @JoinColumn(name = "to_town_id")
    private TownEntity toTown;

    @ManyToOne
    private PlaneEntity plane;

    @ManyToOne(optional = false)
    @JoinColumn(name = "passenger_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private PassengerEntity passenger;
}
