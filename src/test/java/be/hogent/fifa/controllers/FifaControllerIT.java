package be.hogent.fifa.controllers;

import be.hogent.fifa.domain.Stadion;
import be.hogent.fifa.domain.Wedstrijd;
import be.hogent.fifa.domain.WedstrijdTicket;
import be.hogent.fifa.repositories.StadionDao;
import be.hogent.fifa.repositories.WedstrijdDao;
import be.hogent.fifa.repositories.WedstrijdTicketDao;
import be.hogent.fifa.repositories.inmemory.InMemDB;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FifaControllerIT {
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;
    @Value("${spring.datasource.driver-class-name}")
    String driverClassName;

    private Flyway flyway;

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    StadionDao stadionDao;
    @Autowired
    WedstrijdDao wedstrijdDao;
    @Autowired
    WedstrijdTicketDao wedstrijdTicketDao;

    @Autowired
    FifaController subject;

    @BeforeAll
    void setup() {
        flyway = Flyway.configure()
                .dataSource(url, username, password)
                .baselineOnMigrate(true)
                .validateOnMigrate(true)
                .load();
        flyway.migrate();

        jdbcTemplate = new NamedParameterJdbcTemplate(
                DataSourceBuilder.create()
                        .url(url)
                        .username(username)
                        .password(password)
                        .build()
        );
    }

    @AfterAll
    void tearDown() {
        flyway.clean();
    }

    @Test
    void testShowHomePage() {
        //given
        Model model = Mockito.mock(Model.class);

        //when
        var result = subject.showHomePage(model, null, null);

        //then
        assertThat(result).isEqualTo("stadionForm");
    }

    @Test
    void testShowHomePage_verkocht() {
        //given
        Model model = Mockito.mock(Model.class);

        //when
        var result = subject.showHomePage(model, 1, null);

        //then
        assertThat(result).isEqualTo("stadionForm");
        verify(model).addAttribute("verkocht", 1);
    }

    @Test
    void testShowHomePage_uitverkocht() {
        //given
        Model model = Mockito.mock(Model.class);

        //when
        var result = subject.showHomePage(model, null, true);

        //then
        assertThat(result).isEqualTo("stadionForm");
        verify(model).addAttribute("uitverkocht", true);
    }

    @Test
    void testToonWedstrijden() throws Exception {
        //given
        Map<UUID, Stadion> stadions = (Map<UUID, Stadion>) ReflectionTestUtils.getField(InMemDB.DB, "stadions");
        stadions.values()
                .forEach(stadion -> {
                    Stadion persistedStadion = new Stadion(stadion.getNaam(), stadion.getAantalPlaatsen());
                    stadionDao.insert(persistedStadion);
                    stadion.getWedstrijden()
                            .forEach(wedstrijd ->
                                    wedstrijdDao.insert(new Wedstrijd(wedstrijd.getLand1(), wedstrijd.getLand2(),
                                            wedstrijd.getTijdstip(), persistedStadion,
                                            wedstrijd.getAantalBeschikbarePlaatsen())));
                });
        Stadion stadion = stadions.values().stream().findFirst().get();

        StadionCommand stadionCommand = new StadionCommand();
        stadionCommand.setStadionNaam(stadion.getNaam());

        Model model = Mockito.mock(Model.class);
        ArgumentCaptor<String> stadionNameCaptor = ArgumentCaptor.forClass(String.class);
        Class<List<Wedstrijd>> clazz = (Class<List<Wedstrijd>>) (Class) ArrayList.class;
        ArgumentCaptor<List<Wedstrijd>> wedstrijdenCaptor = ArgumentCaptor.forClass(clazz);
        when(model.addAttribute(eq("stadionNaam"), stadionNameCaptor.capture())).thenReturn(model);
        when(model.addAttribute(eq("wedstrijden"), wedstrijdenCaptor.capture())).thenReturn(model);

        //when
        var result = subject.toonWedstrijden(stadionCommand, model);

        //then
        assertThat(result).isEqualTo("wedstrijdenView");

        assertThat(stadionNameCaptor.getValue()).isEqualTo(stadion.getNaam());
        List<Wedstrijd> weds = stadionDao.getByNaam(stadion.getNaam()).getWedstrijden();
        assertThat(wedstrijdenCaptor.getValue()).containsAll(weds);
    }

    @Test
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
        Stadion stadion = stadions.values().stream().findFirst().get();

        Model model = Mockito.mock(Model.class);
        ArgumentCaptor<Wedstrijd> wedstrijdCaptor = ArgumentCaptor.forClass(Wedstrijd.class);
        when(model.addAttribute(eq("wedstrijd"), wedstrijdCaptor.capture())).thenReturn(model);
        when(model.addAttribute(eq("ticketsCommand"), any(TicketsCommand.class))).thenReturn(model);

        Wedstrijd wedstrijd = stadionDao.getByNaam(stadion.getNaam()).getWedstrijden().get(0);

        //when
        var result = subject.getWedstrijd(wedstrijd.getId().toString(), model);

        //then
        assertThat(result).isEqualTo("wedstrijdView");
        assertThat(wedstrijdCaptor.getValue()).isEqualTo(wedstrijd);
    }

}
