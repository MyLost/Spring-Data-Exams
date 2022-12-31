package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CarDto;
import softuni.exam.models.dto.CarWrapperDto;
import softuni.exam.models.entity.CarEntity;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
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
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {

    private final Gson gson;
    private final Validator validator;
    private final ModelMapper mapper;

    private final CarRepository carRepository;

    public CarServiceImpl(Gson gson, Validator validator, ModelMapper mapper, CarRepository carRepository) {
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
        this.carRepository = carRepository;
    }

    @Override
    public boolean areImported() {

        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsFromFile() throws IOException {
        return Files.readString(Paths.CARS_PATH);
    }

    @Override
    public String importCars() throws IOException, JAXBException {

        final FileReader fileReader = new FileReader(Paths.CARS_PATH.toFile());

        final JAXBContext context = JAXBContext.newInstance(CarWrapperDto.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final CarWrapperDto cars = (CarWrapperDto) unmarshaller.unmarshal(fileReader);

        return cars
                .getCars()
                .stream()
                .map(this::create)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String create(CarDto dto) {

        Set<ConstraintViolation<CarDto>> errors = this.validator.validate(dto);

        if (!errors.isEmpty()) {
            return Messages.INVALID + Messages.CAR;
        }

        Optional<CarEntity> entity =
                this.carRepository.findByPlateNumber(dto.getPlateNumber());

        if (entity.isPresent()) {
            return Messages.INVALID + Messages.CAR;
        }

        CarEntity car = this.mapper.map(dto, CarEntity.class);

        this.carRepository.save(car);

        return Messages.SUCCESSFULLY + Messages.CAR + Messages.INTERVAL + car.getCarMake() + Messages.DASH + car.getCarModel();
    }

}
