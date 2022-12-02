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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name="shops")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShopEntity extends BaseEntity {

    @Column(unique = true)
    @Size(min =  4)
    @NotNull
    private String name;

    @Column(precision = 19, scale = 2)
    @Min(20000)
    @NotNull
    private BigDecimal income;

    @NotNull
    private String address;

    @Column(name="employee_count")
    @NotNull
    private Integer employeeCount;

    @Column(name="shop_area")
    @NotNull
    private Integer shopArea;

    @ManyToOne
    private TownEntity town;

    @Override
    public String toString() {
        return name + OutputMessages.DASH + income.toString();
    }
}
