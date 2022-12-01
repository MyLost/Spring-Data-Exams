package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.AgentEntity;

import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<AgentEntity, Long> {

    Optional<AgentEntity> findByEmail(String email);

    Optional<AgentEntity> findByFirstName(String firstName);
}
