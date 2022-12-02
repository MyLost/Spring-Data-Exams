package exam.service.impl;

import exam.model.dtos.TownDto;
import exam.model.dtos.TownWrapperDto;
import exam.model.entities.TownEntity;
import exam.repository.TownRepository;
import exam.service.TownService;
import exam.util.OutputMessages;
import exam.util.Paths;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TownServiceImpl implements TownService {

    private final TownRepository townRepository;

    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public TownServiceImpl(TownRepository townRepository, Validator validator, ModelMapper mapper) {
        this.townRepository = townRepository;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(Paths.TOWNS_PATH);
    }

    @Override
    public String importTowns() throws JAXBException, FileNotFoundException {
        final FileReader fileReader = new FileReader(Paths.TOWNS_PATH.toFile());

        final JAXBContext context = JAXBContext.newInstance(TownWrapperDto.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final TownWrapperDto townsDto = (TownWrapperDto) unmarshaller.unmarshal(fileReader);


        return townsDto
                .getTowns()
                .stream()
                .map(this::createTown)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String createTown(TownDto dto) {

        Set<ConstraintViolation<TownDto>> errors =
                this.validator.validate(dto);

        if (!errors.isEmpty()) {
            return OutputMessages.INVALID + OutputMessages.TOWN;
        }

        Optional<TownEntity> townExist = this.townRepository.findByName(dto.getName());

        if (townExist.isPresent()) {
            return OutputMessages.INVALID + OutputMessages.TOWN;
        }

        TownEntity town = this.mapper.map(dto, TownEntity.class);

        this.townRepository.save(town);

        return OutputMessages.SUCCESSFULLY + OutputMessages.TOWN + OutputMessages.INTERVAL + town.getName();
    }
}
