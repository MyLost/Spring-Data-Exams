package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dtos.CustomerDto;
import exam.model.entities.CustomerEntity;
import exam.model.entities.TownEntity;
import exam.repository.CustomerRepository;
import exam.repository.TownRepository;
import exam.service.CustomerService;
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

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final TownRepository townRepository;

    private final Gson gson;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, TownRepository townRepository, Gson gson, Validator validator, ModelMapper mapper) {
        this.customerRepository = customerRepository;
        this.townRepository = townRepository;
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public boolean areImported() {
        return this.customerRepository.count() > 0;
    }

    @Override
    public String readCustomersFileContent() throws IOException {
        return Files.readString(Paths.CUSTOMERS_PATH);
    }

    @Override
    public String importCustomers() throws IOException {
        final String json = this.readCustomersFileContent();

        final CustomerDto[] customersDto = this.gson.fromJson(json, CustomerDto[].class);

        final List<String> result = new ArrayList<>();

        for (CustomerDto customerDto : customersDto) {

            final Set<ConstraintViolation<CustomerDto>> validationErrors = this.validator.validate(customerDto);

            if (validationErrors.isEmpty()) {

                final Optional<CustomerEntity> customerEntity = this.customerRepository.findByEmail(customerDto.getEmail());

                if (customerEntity.isEmpty()) {

                    CustomerEntity entity = this.mapper.map(customerDto, CustomerEntity.class);

                    TownEntity town = this.townRepository
                            .findByName(customerDto.getTown().getName())
                            .orElseThrow(NoSuchElementException::new);

                    entity.setTown(town);

                    this.customerRepository.save(entity);

                    final String msg = OutputMessages.SUCCESSFULLY + OutputMessages.CUSTOMER + OutputMessages.INTERVAL + entity;

                    result.add(msg);

                } else {
                    result.add(OutputMessages.INVALID + OutputMessages.CUSTOMER);
                }

            } else {
                result.add(OutputMessages.INVALID + OutputMessages.CUSTOMER);
            }
        }
        return String.join(System.lineSeparator(), result);

    }
}
