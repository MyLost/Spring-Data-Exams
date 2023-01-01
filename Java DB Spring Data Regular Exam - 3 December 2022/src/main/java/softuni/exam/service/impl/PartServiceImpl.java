package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PartDto;
import softuni.exam.models.entity.PartEntity;
import softuni.exam.repository.PartRepository;
import softuni.exam.service.PartService;
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
public class PartServiceImpl implements PartService {

    private final Gson gson;
    private final Validator validator;
    private final ModelMapper mapper;

    private final PartRepository partRepository;

    public PartServiceImpl(Gson gson, Validator validator, ModelMapper mapper, PartRepository partRepository) {
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
        this.partRepository = partRepository;
    }

    @Override
    public boolean areImported() {

        return this.partRepository.count() > 0;
    }

    @Override
    public String readPartsFileContent() throws IOException {
        return Files.readString(Paths.PARTS_PATH);
    }

    @Override
    public String importParts() throws IOException {

        String jsonFile = this.readPartsFileContent();

        PartDto[] parts = this.gson.fromJson(jsonFile, PartDto[].class);

        List<String> result = new ArrayList<>();

        for (PartDto part : parts) {

            Set<ConstraintViolation<PartDto>> errors = this.validator.validate(part);

            if(errors.isEmpty()) {

                final Optional<PartEntity> partEntity = this.partRepository.findByPartName(part.getPartName());

                if(partEntity.isEmpty()) {

                    PartEntity entity = this.mapper.map(part, PartEntity.class);

                    this.partRepository.save(entity);

                    result.add(Messages.SUCCESSFULLY + Messages.PART + Messages.INTERVAL + part.getPartName() +
                            Messages.DASH + part.getPrice());
                } else {
                    result.add(Messages.INVALID + Messages.PART);
                }
            } else {
                result.add(Messages.INVALID + Messages.PART);
            }
        }
        return String.join(System.lineSeparator(), result);
    }
}
