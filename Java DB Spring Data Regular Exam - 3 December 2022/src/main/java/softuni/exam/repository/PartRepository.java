package softuni.exam.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.PartEntity;

import java.util.Optional;

@Repository
public interface PartRepository extends JpaRepository<PartEntity, Long> {

    Optional<PartEntity> findByPartName(String partName);
}
