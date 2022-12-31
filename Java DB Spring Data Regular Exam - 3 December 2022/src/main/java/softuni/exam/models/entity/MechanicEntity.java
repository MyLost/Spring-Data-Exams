package softuni.exam.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="mechanics")
public class MechanicEntity extends BaseEntity {

    @Column(name="first_name", nullable = false, unique = true)
    @Size(min = 2)
    private String firstName;

    @Column(name="last_name", nullable = false)
    @Size(min = 2)
    private String lastName;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Size(min=2)
    @Column(unique = true, nullable = false)
    private String phone;

    @OneToMany(mappedBy = "mechanic", targetEntity = TaskEntity.class)
    private List<TaskEntity> tasks;
 }
