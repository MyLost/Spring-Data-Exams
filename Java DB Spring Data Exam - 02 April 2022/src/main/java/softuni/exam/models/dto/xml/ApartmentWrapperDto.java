package softuni.exam.models.dto.xml;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@XmlRootElement(name="apartments")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApartmentWrapperDto {

    @XmlElement(name="apartment")
    List<ApartmentDto> apartments;
}
