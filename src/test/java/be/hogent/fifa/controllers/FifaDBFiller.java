package be.hogent.fifa.controllers;

import be.hogent.fifa.domain.Stadion;
import be.hogent.fifa.domain.Wedstrijd;
import be.hogent.fifa.domain.WedstrijdTicket;
import be.hogent.fifa.repositories.StadionDao;
import be.hogent.fifa.repositories.WedstrijdDao;
import be.hogent.fifa.repositories.WedstrijdTicketDao;
import be.hogent.fifa.repositories.inmemory.InMemDB;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;
import java.util.UUID;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled
public class FifaDBFiller {
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;
    @Value("${spring.datasource.driver-class-name}")
    String driverClassName;

    private Flyway flyway;

    @Autowired
    StadionDao stadionDao;
    @Autowired
    WedstrijdDao wedstrijdDao;
    @Autowired
    WedstrijdTicketDao wedstrijdTicketDao;

    @BeforeAll
    void setup() {
        flyway = Flyway.configure()
                .dataSource(url, username, password)
                .baselineOnMigrate(true)
                .validateOnMigrate(true)
                .load();
        flyway.migrate();
    }

    @AfterAll
    void tearDown() {
        //flyway.clean();
    }

    @Test
    @Disabled
    void testGetWedstrijd() throws Exception {
        //given
        Map<UUID, Stadion> stadions = (Map<UUID, Stadion>) ReflectionTestUtils.getField(InMemDB.DB, "stadions");
        Map<UUID, WedstrijdTicket> tickets = (Map<UUID, WedstrijdTicket>) ReflectionTestUtils.getField(InMemDB.DB, "tickets");
        stadions.values()
                .forEach(stadion -> {
                    Stadion persistedStadion = new Stadion(stadion.getNaam(), stadion.getAantalPlaatsen());
                    stadionDao.insert(persistedStadion);
                    stadion.getWedstrijden()
                            .forEach(wedstrijd -> {
                                Wedstrijd persistedWedstrijd = new Wedstrijd(wedstrijd.getLand1(), wedstrijd.getLand2(),
                                        wedstrijd.getTijdstip(), persistedStadion,
                                        wedstrijd.getAantalBeschikbarePlaatsen());
                                wedstrijdDao.insert(persistedWedstrijd);
                                tickets.values().forEach(wedstrijdTicket ->
                                        wedstrijdTicketDao.insert(new WedstrijdTicket(
                                                wedstrijdTicket.getEmail(), wedstrijdTicket.getVoetbalCode1(),
                                                wedstrijdTicket.getVoetbalCode2(), persistedWedstrijd)
                                        )
                                );
                            });
                });
    }

}
