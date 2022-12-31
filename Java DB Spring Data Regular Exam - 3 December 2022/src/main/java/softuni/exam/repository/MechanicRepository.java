package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.MechanicEntity;

import java.util.Optional;

@Repository
public interface MechanicRepository extends JpaRepository<MechanicEntity, Long> {

    Optional<MechanicEntity> findByFirstName(String firstName);

    Optional<MechanicEntity> findByFirstNameAndEmail(String firstName, String email);

    Optional<MechanicEntity> findByEmail(String email);
}
