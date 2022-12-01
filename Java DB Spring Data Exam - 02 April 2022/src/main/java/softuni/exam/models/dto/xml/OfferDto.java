package softuni.exam.models.dto.xml;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.exam.models.dto.json.AgentDto;
import softuni.exam.util.LocalDateAdapter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@XmlRootElement(name = "offer")
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferDto {

    @NotNull
    @Positive
    @XmlElement
    private BigDecimal price;

    @XmlElement
    @NotNull
    private AgentNameDto agent;

    @XmlElement
    @NotNull
    private ApartmentIdDto apartment;

    @XmlElement
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @NotNull
    private LocalDate publishedOn;
}
