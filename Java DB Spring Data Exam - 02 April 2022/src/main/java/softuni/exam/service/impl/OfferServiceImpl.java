package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.ApartmentType;
import softuni.exam.models.dto.xml.OfferDto;
import softuni.exam.models.dto.xml.OfferWrapperDto;
import softuni.exam.models.entity.AgentEntity;
import softuni.exam.models.entity.ApartmentEntity;
import softuni.exam.models.entity.OfferEntity;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.OfferService;
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
public class OfferServiceImpl implements OfferService {

    private final ApartmentType apartmentType = ApartmentType.three_rooms;

    private final Validator validator;
    private final ModelMapper mapper;

    private final OfferRepository offerRepository;
    private final AgentRepository agentRepository;
    private final ApartmentRepository apartmentRepository;

    @Autowired
    public OfferServiceImpl(Validator validator, ModelMapper mapper, OfferRepository offerRepository, AgentRepository agentRepository, ApartmentRepository apartmentRepository) {
        this.validator = validator;
        this.mapper = mapper;
        this.offerRepository = offerRepository;
        this.agentRepository = agentRepository;
        this.apartmentRepository = apartmentRepository;
    }


    @Override
    public boolean areImported() {
        return this.offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Paths.OFFERS_PATH);
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        final FileReader fileReader = new FileReader(Paths.OFFERS_PATH.toFile());

        final JAXBContext context = JAXBContext.newInstance(OfferWrapperDto.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final OfferWrapperDto offersDto = (OfferWrapperDto) unmarshaller.unmarshal(fileReader);


        return offersDto
                .getOffers()
                .stream()
                .map(this::create)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    @Override
    public String exportOffers() {
        return this.offerRepository.findByApartment_ApartmentTypeOrderByApartment_AreaDescPriceAsc(apartmentType)
                .orElseThrow(NoSuchElementException::new)
                .stream()
                .map(OfferEntity::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String create(OfferDto dto) {

        Set<ConstraintViolation<OfferDto>> errors = this.validator.validate(dto);

        if (!errors.isEmpty()) {
            return Messages.INVALID + Messages.OFFER;
        }

        Optional<AgentEntity> agentExist =
                this.agentRepository.findByFirstName(dto.getAgent().getName());

        if (agentExist.isEmpty()) {
            return Messages.INVALID + Messages.OFFER;
        }

        OfferEntity entity = this.mapper.map(dto, OfferEntity.class);

        Optional<ApartmentEntity> apartment = this.apartmentRepository.findById(dto.getApartment().getId());

        entity.setAgent(agentExist.get());

        entity.setApartment(apartment.get());

        this.offerRepository.save(entity);

        return Messages.SUCCESSFULLY + Messages.OFFER + Messages.INTERVAL + entity.importInfo();
    }
}
