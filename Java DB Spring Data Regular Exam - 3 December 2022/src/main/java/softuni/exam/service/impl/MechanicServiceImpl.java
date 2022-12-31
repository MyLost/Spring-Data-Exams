package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.MechanicDto;
import softuni.exam.models.entity.MechanicEntity;
import softuni.exam.models.entity.PartEntity;
import softuni.exam.repository.MechanicRepository;
import softuni.exam.service.MechanicService;
import softuni.exam.util.Messages;
import softuni.exam.util.Paths;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MechanicServiceImpl implements MechanicService {

    private final Gson gson;
    private final Validator validator;
    private final MechanicRepository mechanicRepository;
    private final ModelMapper modelMapper;

    public MechanicServiceImpl(Gson gson, Validator validator, MechanicRepository mechanicRepository, ModelMapper modelMapper) {
        this.gson = gson;
        this.validator = validator;
        this.mechanicRepository = mechanicRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.mechanicRepository.count() > 0;
    }

    @Override
    public String readMechanicsFromFile() throws IOException {
        return Files.readString(Paths.MECHANICS_PATH);
    }

    @Override
    public String importMechanics() throws IOException {

        final String jsonFile = this.readMechanicsFromFile();

        final MechanicDto[] mechanics = this.gson.fromJson(jsonFile, MechanicDto[].class);

        final List<String> result = new ArrayList<>();

        for (MechanicDto mechanic : mechanics) {

            final Set<ConstraintViolation<MechanicDto>> errors = this.validator.validate(mechanic);

            if(errors.isEmpty()) {

                final Optional<MechanicEntity> mechanicEntity = this.mechanicRepository.findByEmail(mechanic.getEmail());

                if(mechanicEntity.isEmpty()) {

                    MechanicEntity entity = this.modelMapper.map(mechanic, MechanicEntity.class);

                    this.mechanicRepository.save(entity);

                    result.add(Messages.SUCCESSFULLY + Messages.MECHANIC + Messages.INTERVAL + mechanic.getFirstName() + Messages.INTERVAL + mechanic.getLastName());
                } else {
                    result.add(Messages.INVALID + Messages.MECHANIC);
                }
            } else {
                result.add(Messages.INVALID + Messages.MECHANIC);
            }
        }
        return String.join(System.lineSeparator(), result);
    }
}
