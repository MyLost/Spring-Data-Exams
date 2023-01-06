package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.TaskEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    @Query(value = "select * from tasks t join cars c on t.car_id = c.id join mechanics m on t.mechanic_id = m.id where c.car_type = :carType order by t.price DESC;", nativeQuery = true)
    Optional<List<TaskEntity>> getAllByHighestPrice(String carType);
}
