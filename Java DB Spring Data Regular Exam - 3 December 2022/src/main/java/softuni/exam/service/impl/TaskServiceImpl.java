package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.tasks.TaskDto;
import softuni.exam.models.dto.tasks.TaskWrapperDto;
import softuni.exam.models.entity.CarEntity;
import softuni.exam.models.entity.MechanicEntity;
import softuni.exam.models.entity.PartEntity;
import softuni.exam.models.entity.TaskEntity;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.MechanicRepository;
import softuni.exam.repository.PartRepository;
import softuni.exam.repository.TaskRepository;
import softuni.exam.service.TaskService;
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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final Validator validator;
    private final ModelMapper mapper;

    private final CarRepository carRepository;
    private final MechanicRepository mechanicRepository;
    private final PartRepository partRepository;
    private final TaskRepository taskRepository;

    public TaskServiceImpl(Validator validator, ModelMapper mapper, CarRepository offerRepository, MechanicRepository agentRepository, PartRepository apartmentRepository, CarRepository carRepository, MechanicRepository mechanicRepository, PartRepository partRepository, TaskRepository taskRepository) {
        this.validator = validator;
        this.mapper = mapper;
        this.carRepository = carRepository;
        this.mechanicRepository = mechanicRepository;
        this.partRepository = partRepository;

        this.taskRepository = taskRepository;
    }

    @Override
    public boolean areImported() {
        return this.taskRepository.count() > 0;
    }

    @Override
    public String readTasksFileContent() throws IOException {
        return Files.readString(Paths.TASKS_PATH);
    }

    @Override
    public String importTasks() throws IOException, JAXBException {

        final FileReader fileReader = new FileReader(Paths.TASKS_PATH.toFile());

        final JAXBContext context = JAXBContext.newInstance(TaskWrapperDto.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final TaskWrapperDto tasksDto = (TaskWrapperDto) unmarshaller.unmarshal(fileReader);

        return tasksDto
                .getTasks()
                .stream()
                .map(this::create)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String create(TaskDto dto) {

        Set<ConstraintViolation<TaskDto>> errors = this.validator.validate(dto);

        if (!errors.isEmpty()) {
            return Messages.INVALID + Messages.TASK;
        }

        final Optional<MechanicEntity> mechanicEntity = this.mechanicRepository.findByFirstName(dto.getMechanic().getFirstName());


        if (mechanicEntity.isEmpty()) {
            return Messages.INVALID + Messages.TASK;
        }

        TaskEntity entity = this.mapper.map(dto, TaskEntity.class);

        Optional<CarEntity> car = this.carRepository.findById(dto.getCar().getId());
        Optional<MechanicEntity> mechanic = this.mechanicRepository.findByFirstName(dto.getMechanic().getFirstName());
        Optional<PartEntity> part = this.partRepository.findById(dto.getPart().getId());

        entity.setCar(car.get());
        entity.setMechanic(mechanic.get());
        entity.setPart(part.get());

        this.taskRepository.save(entity);

        return Messages.SUCCESSFULLY + Messages.TASK + Messages.INTERVAL + entity.getPrice();
    }

    @Override
    public String getCoupeCarTasksOrderByPrice() {

        return this.taskRepository.getAllByHighestPrice("coupe")
                .orElseThrow(NoSuchElementException::new)
                .stream()
                .map(TaskEntity::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
