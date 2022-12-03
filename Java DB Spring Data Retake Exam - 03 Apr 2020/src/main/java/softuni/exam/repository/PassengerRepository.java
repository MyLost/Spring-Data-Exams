package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entities.PassengerEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerRepository  extends JpaRepository<PassengerEntity, Long> {

    Optional<PassengerEntity> findFirstByEmail(String email);

    @Query("SELECT p FROM PassengerEntity AS p ORDER BY p.tickets.size DESC, p.email ASC")
    Optional<List<PassengerEntity>> findAllByOrderByTicketsCountDescEmailAsc();
}
