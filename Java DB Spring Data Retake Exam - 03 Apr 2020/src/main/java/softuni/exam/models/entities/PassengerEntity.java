package softuni.exam.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="passengers")
public class PassengerEntity extends BaseEntity {


    @Column(name="first_name")
    @Size(min = 2)
    private String firstName;

    @Column(name="last_name")
    @Size(min = 2)
    private String lastName;

    @Positive
    private Integer age;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    @ManyToOne
    private TownEntity town;

    @OneToMany(mappedBy = "passenger", targetEntity = TicketEntity.class)
    private List<TicketEntity> tickets;

    @Override
    public String toString() {
        return "Passenger " + firstName + " " + lastName + System.lineSeparator() +
                "   Email - " + email + System.lineSeparator() +
                "   Phone - " + phoneNumber + System.lineSeparator() +
                String.format("   Number of tickets - %d%n",tickets.size());
    }
}
