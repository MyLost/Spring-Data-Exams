package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dtos.LaptopDto;
import exam.model.entities.LaptopEntity;
import exam.model.entities.ShopEntity;
import exam.repository.LaptopRepository;
import exam.repository.ShopRepository;
import exam.service.LaptopService;
import exam.util.OutputMessages;
import exam.util.Paths;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LaptopServiceImpl implements LaptopService {

    private final LaptopRepository laptopRepository;
    private final ShopRepository shopRepository;

    private final Gson gson;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public LaptopServiceImpl(LaptopRepository laptopRepository, ShopRepository shopRepository, Gson gson, Validator validator, ModelMapper mapper) {
        this.laptopRepository = laptopRepository;
        this.shopRepository = shopRepository;
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public boolean areImported() {
        return this.laptopRepository.count() > 0;
    }

    @Override
    public String readLaptopsFileContent() throws IOException {
        return Files.readString(Paths.LAPTOPS_PATH);
    }

    @Override
    public String importLaptops() throws IOException {
        final String json = this.readLaptopsFileContent();

        final LaptopDto[] importLaptops = this.gson.fromJson(json, LaptopDto[].class);

        final List<String> result = new ArrayList<>();

        for (LaptopDto importLaptop : importLaptops) {

            final Set<ConstraintViolation<LaptopDto>> validationErrors =
                    this.validator.validate(importLaptop);

            if (validationErrors.isEmpty()) {

                final Optional<LaptopEntity> laptopExist =
                        this.laptopRepository.findByMacAddress(importLaptop.getMacAddress());

                boolean canAdded = laptopExist.isEmpty();

                if (canAdded) {

                    LaptopEntity laptop = this.mapper.map(importLaptop, LaptopEntity.class);

                    ShopEntity shop = this.shopRepository
                            .findByName(importLaptop.getShop().getName())
                            .orElseThrow(NoSuchElementException::new);

                    laptop.setShop(shop);

                    this.laptopRepository.save(laptop);

                    final String msg =
                            OutputMessages.SUCCESSFULLY + OutputMessages.LAPTOP + OutputMessages.INTERVAL + laptop.importInfo();

                    result.add(msg);

                } else {
                    result.add(OutputMessages.INVALID + OutputMessages.LAPTOP);
                }

            } else {
                result.add(OutputMessages.INVALID + OutputMessages.LAPTOP);
            }
        }
        return String.join(System.lineSeparator(), result);
    }

    @Override
    public String exportBestLaptops() {
        return this.laptopRepository
                .findAllByOrderByCpuSpeedDescRamDescStorageDescMacAddressAsc()
                .orElseThrow(NoSuchElementException::new)
                .stream()
                .map(LaptopEntity::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
