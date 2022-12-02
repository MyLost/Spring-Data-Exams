package exam.model.entities;

import exam.model.enums.WarrantyType;
import exam.util.OutputMessages;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name="laptops")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LaptopEntity extends  BaseEntity {

    @Column(name="mac_address")
    @Size(min=9)
    @NotNull
    private String macAddress;

    @Column(name="cpu_speed")
    @NotNull
    private Double cpuSpeed;

    @NotNull
    private Integer ram;

    @NotNull
    private Integer storage;

    @Column(columnDefinition = "text")
    @Size(min = 10)
    @NotNull
    private String description;

    @Column(precision = 19, scale = 2)
    @Positive
    @NotNull
    private BigDecimal price;

    @Column(name="warranty_type")
    @NotNull
    private WarrantyType warrantyType;

    @ManyToOne
    private ShopEntity shop;

    @Override
    public String toString() {
        return String.format(OutputMessages.PRINT_LAPTOP, macAddress) + System.lineSeparator() +
                String.format(OutputMessages.PRINT_CPU, cpuSpeed) + System.lineSeparator() +
                String.format(OutputMessages.PRINT_RAM, ram) + System.lineSeparator() +
                String.format(OutputMessages.PRINT_STORAGE, storage) + System.lineSeparator() +
                String.format(OutputMessages.PRINT_PRICE, price) + System.lineSeparator() +
                String.format(OutputMessages.PRINT_SHOP, shop.getName()) + System.lineSeparator() +
                String.format(OutputMessages.PRINT_TOWN, shop.getTown().getName()) + System.lineSeparator();
    }

    public String importInfo(){
        return macAddress + OutputMessages.DASH +
                String.format(OutputMessages.FORMAT_DOUBLE, cpuSpeed) + OutputMessages.DASH +
                ram + OutputMessages.DASH +
                storage;
    }
}
