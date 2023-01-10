package softuni.exam.models.dto.tasks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.exam.models.dto.CarDto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@XmlRootElement(name="tasks")
@XmlAccessorType(XmlAccessType.FIELD)
public class TaskWrapperDto {

    @XmlElement(name="task")
    List<TaskDto> tasks;

}
