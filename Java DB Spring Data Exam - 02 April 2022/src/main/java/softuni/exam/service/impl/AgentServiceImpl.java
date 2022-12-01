package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.json.AgentDto;
import softuni.exam.models.entity.AgentEntity;
import softuni.exam.models.entity.TownEntity;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.AgentService;
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
public class AgentServiceImpl implements AgentService {

    private final Gson gson;
    private final Validator validator;
    private final ModelMapper mapper;

    private final AgentRepository agentRepository;
    private final TownRepository townRepository;

    public AgentServiceImpl(Gson gson, Validator validator, ModelMapper mapper, AgentRepository agentRepository, TownRepository townRepository) {
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
        this.agentRepository = agentRepository;
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() {
        return this.agentRepository.count() > 0;
    }

    @Override
    public String readAgentsFromFile() throws IOException {
        return Files.readString(Paths.AGENTS_PATH);
    }

    @Override
    public String importAgents() throws IOException {

        final String jsonFile = this.readAgentsFromFile();

        final AgentDto[] agents = this.gson.fromJson(jsonFile, AgentDto[].class);

        final List<String> result = new ArrayList<>();

        for (AgentDto agent : agents) {

            final Set<ConstraintViolation<AgentDto>> errors = this.validator.validate(agent);

            if(errors.isEmpty()) {

                final Optional<AgentEntity> agentExistByName = this.agentRepository.findByFirstName(agent.getFirstName());
                final Optional<AgentEntity> agentExistByEmail = this.agentRepository.findByEmail(agent.getEmail());
                final Optional<TownEntity> town = this.townRepository.findByTownName(agent.getTownName());

                if(agentExistByName.isEmpty() && agentExistByEmail.isEmpty() && town.isPresent()) {

                    AgentEntity entity = this.mapper.map(agent, AgentEntity.class);

                    entity.setTown(town.get());

                    this.agentRepository.save(entity);

                    result.add(Messages.SUCCESSFULLY + Messages.AGENT + Messages.DASH + agent);

                } else {
                    result.add(Messages.INVALID + Messages.AGENT);
                }
            } else {
                result.add(Messages.INVALID + Messages.AGENT);
            }
        }

        return String.join(System.lineSeparator(), result);
    }
}
