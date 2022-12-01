package softuni.exam.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.exam.models.ApartmentType;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name="apartments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApartmentEntity extends BaseEntity {

    @Column(name="apartment_type")
    @Enumerated
    @NotNull
    private ApartmentType apartmentType;

    @Min(value=40)
    @NotNull
    private Double area;

    @OneToOne
    private TownEntity town;

    @OneToMany(mappedBy = "apartment", targetEntity = OfferEntity.class)
    private List<OfferEntity> offers;
}
