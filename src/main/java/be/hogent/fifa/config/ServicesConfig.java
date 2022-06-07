package be.hogent.fifa.config;

import be.hogent.fifa.repositories.StadionDao;
import be.hogent.fifa.repositories.WedstrijdDao;
import be.hogent.fifa.repositories.WedstrijdTicketDao;
import be.hogent.fifa.services.DefaultVoetbalServiceImpl;
import be.hogent.fifa.services.VoetbalService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfig {
    @Bean
    public VoetbalService voetbalService(StadionDao stadionDao, WedstrijdDao wedstrijdDao, WedstrijdTicketDao wedstrijdTicketDao) {
        return new DefaultVoetbalServiceImpl(stadionDao, wedstrijdDao, wedstrijdTicketDao);
    }
}
