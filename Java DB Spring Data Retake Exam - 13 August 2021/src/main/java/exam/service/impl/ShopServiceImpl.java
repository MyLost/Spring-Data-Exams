package exam.service.impl;

import exam.model.dtos.ShopDto;
import exam.model.dtos.ShopWrapperDto;
import exam.model.entities.ShopEntity;
import exam.model.entities.TownEntity;
import exam.repository.ShopRepository;
import exam.repository.TownRepository;
import exam.service.ShopService;
import exam.util.OutputMessages;
import exam.util.Paths;
import org.modelmapper.ModelMapper;
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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;
    private final TownRepository townRepository;

    private final Validator validator;
    private final ModelMapper mapper;

    public ShopServiceImpl(ShopRepository shopRepository, TownRepository townRepository, Validator validator, ModelMapper mapper) {
        this.shopRepository = shopRepository;
        this.townRepository = townRepository;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public boolean areImported() {
        return this.shopRepository.count() > 0;
    }

    @Override
    public String readShopsFileContent() throws IOException {
        return Files.readString(Paths.SHOPS_PATH);
    }

    @Override
    public String importShops() throws JAXBException, FileNotFoundException {
        final FileReader fileReader = new FileReader(Paths.SHOPS_PATH.toFile());

        final JAXBContext context = JAXBContext.newInstance(ShopWrapperDto.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final ShopWrapperDto shopsDto = (ShopWrapperDto) unmarshaller.unmarshal(fileReader);


        return shopsDto
                .getShops()
                .stream()
                .map(this::create)
                .collect(Collectors.joining(System.lineSeparator()));
    }


    private String create(ShopDto dto) {

        Set<ConstraintViolation<ShopDto>> errors = this.validator.validate(dto);

        if (!errors.isEmpty()) {
            return OutputMessages.INVALID + OutputMessages.SHOP;
        }

        Optional<ShopEntity> shopExist = this.shopRepository.findByName(dto.getName());

        if (shopExist.isPresent()) {
            return OutputMessages.INVALID + OutputMessages.SHOP;
        }

        ShopEntity shop = this.mapper.map(dto, ShopEntity.class);

        TownEntity town =
                this.townRepository
                        .findByName(dto.getTown().getName())
                        .orElseThrow(NoSuchElementException::new);

        shop.setTown(town);

        this.shopRepository.save(shop);

        return OutputMessages.SUCCESSFULLY + OutputMessages.SHOP + OutputMessages.INTERVAL + shop;
    }
}
