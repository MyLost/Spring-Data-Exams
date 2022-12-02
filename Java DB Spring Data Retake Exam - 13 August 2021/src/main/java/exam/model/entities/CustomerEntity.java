package exam.model.entities;

import exam.util.OutputMessages;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "customers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEntity extends BaseEntity {

    @Column(name="first_name")
    @Size(min = 2)
    @NotNull
    private String firstName;

    @Column(name="last_name")
    @Size(min = 2)
    @NotNull
    private String lastName;

    @Column(unique = true)
    @Email
    @NotNull
    private String email;

    @Column(name="registered_on")
    @NotNull
    private LocalDate registeredOn;

    @ManyToOne(optional = false)
    private TownEntity town;

    @Override
    public String toString() {
        return firstName + OutputMessages.INTERVAL + lastName + OutputMessages.DASH + email;
    }

}
