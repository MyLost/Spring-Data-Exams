package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.ApartmentEntity;

import java.util.Optional;

@Repository
public interface ApartmentRepository  extends JpaRepository<ApartmentEntity, Long> {

    Optional<ApartmentEntity> findByTown_TownNameAndArea(String townName, double area);
}
