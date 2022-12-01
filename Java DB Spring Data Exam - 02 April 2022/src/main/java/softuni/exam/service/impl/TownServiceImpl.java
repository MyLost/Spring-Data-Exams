package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.json.TownDto;
import softuni.exam.models.entity.TownEntity;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.Messages;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TownServiceImpl implements TownService {

    private final Gson gson;
    private final Validator validator;
    private final TownRepository townRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TownServiceImpl(Gson gson, Validator validator, TownRepository townRepository, ModelMapper modelMapper) {
        this.gson = gson;
        this.validator = validator;
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
      return Files.readString(Path.of("src", "main", "resources", "files", "json", "towns.json"));

    }

    @Override
    public String importTowns() throws IOException {

        final String jsonFile = this.readTownsFileContent();

        final TownDto[] towns = this.gson.fromJson(jsonFile, TownDto[].class);

        final List<String> result = new ArrayList<>();

        for (TownDto town : towns) {

            final Set<ConstraintViolation<TownDto>> errors = this.validator.validate(town);

            if(errors.isEmpty()) {

                final Optional<TownEntity> townExist = this.townRepository.findByTownName(town.getTownName());

                if(townExist.isEmpty()) {

                    TownEntity entity = this.modelMapper.map(town, TownEntity.class);

                    this.townRepository.save(entity);

                    result.add(Messages.SUCCESSFULLY + Messages.TOWN + Messages.INTERVAL + town);
                } else {
                    result.add(Messages.INVALID + Messages.TOWN);
                }
            } else {
                result.add(Messages.INVALID + Messages.TOWN);
            }
        }
        return String.join(System.lineSeparator(), result);
    }
}
