package be.hogent.fifa.config;

import be.hogent.fifa.rest.FifaRestController;
import be.hogent.fifa.services.VoetbalService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestConfig {
    @Bean
    public FifaRestController fifaRestController(VoetbalService voetbalService) {
        return new FifaRestController(voetbalService);
    }
}
