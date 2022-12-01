package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.xml.ApartmentDto;
import softuni.exam.models.dto.xml.ApartmentWrapperDto;
import softuni.exam.models.entity.ApartmentEntity;
import softuni.exam.models.entity.TownEntity;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.ApartmentService;
import softuni.exam.util.Messages;
import softuni.exam.util.Paths;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApartmentServiceImpl implements ApartmentService {

    private final Validator validator;
    private final ModelMapper mapper;

    private final ApartmentRepository apartmentRepository;
    private final TownRepository townRepository;

    public ApartmentServiceImpl(Validator validator, ModelMapper mapper, ApartmentRepository apartmentRepository, TownRepository townRepository) {
        this.validator = validator;
        this.mapper = mapper;
        this.apartmentRepository = apartmentRepository;
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() {
        return this.apartmentRepository.count() > 0;
    }

    @Override
    public String readApartmentsFromFile() throws IOException {
        return Files.readString(Paths.APARTMENTS_PATH);
    }

    @Override
    public String importApartments() throws IOException, JAXBException {

        final FileReader fileReader = new FileReader(Paths.APARTMENTS_PATH.toFile());

        final JAXBContext context = JAXBContext.newInstance(ApartmentWrapperDto.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final ApartmentWrapperDto apartments = (ApartmentWrapperDto) unmarshaller.unmarshal(fileReader);


        return apartments
                .getApartments()
                .stream()
                .map(this::create)
                .collect(Collectors.joining(System.lineSeparator()));
    }


    private String create(ApartmentDto dto) {

        Set<ConstraintViolation<ApartmentDto>> errors = this.validator.validate(dto);

        if (!errors.isEmpty()) {
            return Messages.INVALID + Messages.APARTMENT;
        }

        Optional<ApartmentEntity> entity =
                this.apartmentRepository.findByTown_TownNameAndArea(dto.getTownName(), dto.getArea());

        if (entity.isPresent()) {
            return Messages.INVALID + Messages.APARTMENT;
        }

        ApartmentEntity apartment = this.mapper.map(dto, ApartmentEntity.class);

        Optional<TownEntity> town = this.townRepository.findByTownName(dto.getTownName());

        apartment.setTown(town.get());

        this.apartmentRepository.save(apartment);

        return Messages.SUCCESSFULLY + Messages.APARTMENT + Messages.INTERVAL + apartment;
    }
}
