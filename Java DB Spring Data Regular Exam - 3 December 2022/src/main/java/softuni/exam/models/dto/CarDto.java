package softuni.exam.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.exam.models.enums.CarType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@XmlRootElement(name = "car")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarDto {

    @NotNull
    @XmlElement
    private CarType carType;

    @Size(min = 2, max = 30)
    @NotNull
    @XmlElement
    private String carMake;

    @Size(min = 2, max = 30)
    @NotNull
    @XmlElement
    private String carModel;

    @Positive
    @NotNull
    @XmlElement
    private Integer year;

    @Size(min = 2, max = 30)
    @NotNull
    @XmlElement
    private String plateNumber;

    @Positive
    @NotNull
    @XmlElement
    private Integer kilometers;

    @Min(1)
    @NotNull
    @XmlElement
    private Double engine;
}
