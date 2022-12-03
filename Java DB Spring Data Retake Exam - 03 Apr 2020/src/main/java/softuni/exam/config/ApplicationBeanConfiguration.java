package softuni.exam.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import softuni.exam.models.dto.wrapper.PlanesWrapperDTO;
import softuni.exam.models.dto.wrapper.TicketsWrapperDTO;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.ValidationUtilImpl;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;


@Configuration
public class ApplicationBeanConfiguration {

New Folder
    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
    }


    @Bean
    public StringBuilder createStringBuilder() {
        return new StringBuilder();
    }

//    @Bean
//    public ValidationUtil validationUtil() {
//        return new ValidationUtilImpl();
//    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

//    @Bean
//    public XmlParser xmlParser(){
//        return new XmlParserImpl();
//    }

    @Bean(name = "planesContext")
    public JAXBContext createPlanesContext() throws JAXBException {
        return JAXBContext.newInstance(PlanesWrapperDTO.class);
    }

    @Bean(name = "ticketsContext")
    public JAXBContext createTicketsContext() throws JAXBException {
        return JAXBContext.newInstance(TicketsWrapperDTO.class);
    }
}
