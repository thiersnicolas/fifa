package be.hogent.fifa.config;

import be.hogent.fifa.controllers.FifaController;
import be.hogent.fifa.controllers.HomeController;
import be.hogent.fifa.controllers.validator.TicketsCommandValidation;
import be.hogent.fifa.services.VoetbalService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfig {
    @Bean
    public HomeController homeController() {
        return new HomeController();
    }

    @Bean
    public FifaController fifaController(VoetbalService voetbalService, TicketsCommandValidation ticketsCommandValidation) {
        return new FifaController(voetbalService, ticketsCommandValidation);
    }

    @Bean
    public TicketsCommandValidation ticketsCommandValidation() {
        return new TicketsCommandValidation();
    }
}
