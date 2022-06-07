package be.hogent.fifa.config;

import be.hogent.fifa.repositories.StadionDao;
import be.hogent.fifa.repositories.WedstrijdDao;
import be.hogent.fifa.repositories.WedstrijdTicketDao;
import be.hogent.fifa.repositories.inmemory.InMemStadionDao;
import be.hogent.fifa.repositories.inmemory.InMemWedstrijdDao;
import be.hogent.fifa.repositories.inmemory.InMemWedstrijdTicketDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("inmemorydb")
public class InMemoryDataProviderConfig {
    @Bean
    public StadionDao stadionDao() {
        return new InMemStadionDao();
    }

    @Bean
    public WedstrijdDao wedstrijdDao() {
        return new InMemWedstrijdDao();
    }

    @Bean
    public WedstrijdTicketDao wedstrijdTicketDao() {
        return new InMemWedstrijdTicketDao();
    }
}
