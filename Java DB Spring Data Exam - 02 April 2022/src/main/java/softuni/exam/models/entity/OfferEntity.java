package softuni.exam.models.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.exam.util.Messages;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="offers")
public class OfferEntity extends BaseEntity {

    @Column(precision = 19, scale = 2)
    @Positive
    @NotNull
    private BigDecimal price;

    @Column(name="published_on")
    @NotNull
    private LocalDate publishedOn;

    @OneToOne
    private ApartmentEntity apartment;

    @OneToOne
    private AgentEntity agent;

    public String importInfo(){
        return String.format(Messages.FORMAT_DOUBLE, this.price);
    }

    @Override
    public String toString() {

        return String.format(Messages.PRINT_AGENT, this.agent.getFirstName(), this.agent.getLastName(), this.getId()) +
                System.lineSeparator() +
                String.format(Messages.PRINT_APARTMENT_AREA, this.apartment.getArea()) + System.lineSeparator() +
                String.format(Messages.PRINT_TOWN, this.apartment.getTown().getTownName()) + System.lineSeparator() +
                String.format(Messages.PRINT_PRICE, this.price);
    }
}
