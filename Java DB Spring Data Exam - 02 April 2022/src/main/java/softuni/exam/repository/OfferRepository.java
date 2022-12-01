package softuni.exam.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.ApartmentType;
import softuni.exam.models.entity.OfferEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<OfferEntity, Long> {
    Optional<List<OfferEntity>> findByApartment_ApartmentTypeOrderByApartment_AreaDescPriceAsc(ApartmentType apartmentType);
}
