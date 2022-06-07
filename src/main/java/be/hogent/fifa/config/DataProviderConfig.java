package be.hogent.fifa.config;

import be.hogent.fifa.repositories.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@EntityScan("be.hogent.fifa.domain")
@Configuration
@Profile("!inmemorydb")
public class DataProviderConfig {
    @Bean
    public StadionDao stadionDao() {
        return new JpaStadionDao();
    }

    @Bean
    public WedstrijdDao wedstrijdDao() {
        return new JpaWedstrijdDao();
    }

    @Bean
    public WedstrijdTicketDao wedstrijdTicketDao() {
        return new JpaTicketDao();
    }
}
