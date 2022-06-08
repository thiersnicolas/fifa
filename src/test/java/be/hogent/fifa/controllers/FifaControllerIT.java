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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.*;

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

        Map<UUID, Stadion> stadions = (Map<UUID, Stadion>) ReflectionTestUtils.getField(InMemDB.DB, "stadions");
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
                            });
                });
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
        Stadion stadion = stadions.values().stream().findFirst().get();
        Wedstrijd wedstrijd = stadionDao.getByNaam(stadion.getNaam()).getWedstrijden().get(0);

        Model model = Mockito.mock(Model.class);
        ArgumentCaptor<Wedstrijd> wedstrijdCaptor = ArgumentCaptor.forClass(Wedstrijd.class);
        when(model.addAttribute(eq("wedstrijd"), wedstrijdCaptor.capture())).thenReturn(model);
        when(model.addAttribute(eq("ticketsCommand"), any(TicketsCommand.class))).thenReturn(model);

        //when
        var result = subject.getWedstrijd(wedstrijd.getId().toString(), model);

        //then
        assertThat(result).isEqualTo("wedstrijdView");
        assertThat(wedstrijdCaptor.getValue()).isEqualTo(wedstrijd);
    }

    @Test
    public void testBestelTickets_noErrors() {
        //given
        Map<UUID, Stadion> stadions = (Map<UUID, Stadion>) ReflectionTestUtils.getField(InMemDB.DB, "stadions");
        Stadion stadion = stadions.values().stream().findFirst().get();
        Wedstrijd wedstrijd = stadionDao.getByNaam(stadion.getNaam()).getWedstrijden().get(0);

        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        String email = "valid@ok.com";
        TicketsCommand ticketsCommand = new TicketsCommand();
        ticketsCommand.setEmail(email);
        ticketsCommand.setAantal("1");
        ticketsCommand.setVoetbalCode1("10");
        ticketsCommand.setVoetbalCode2("25");

        int aantalBeschikbareTicketsVoorTest = wedstrijd.getAantalBeschikbarePlaatsen();

        //when
        var result = subject.bestelTickets(wedstrijd.getId().toString(), ticketsCommand, bindingResult, Mockito.mock(Model.class));

        //then
        assertThat(result).isEqualTo("redirect:/fifa?verkocht=1");
        assertThat(wedstrijdDao.get(wedstrijd.getId()).getAantalBeschikbarePlaatsen())
                .isEqualTo(aantalBeschikbareTicketsVoorTest - 1);

        List<Map<String, Object>> dbResults = jdbcTemplate.queryForList("select * from tickets t where t.email = '" + email + "'", new HashMap<>());
        assertThat(dbResults.size()).isEqualTo(1);

        Map<String, Object> DBresult = dbResults.get(0);
        assertThat(DBresult.get("email")).isEqualTo(email);
        assertThat(DBresult.get("voetbal_code1")).isEqualTo(10);
        assertThat(DBresult.get("voetbal_code2")).isEqualTo(25);
    }

    @Test
    public void testBestelTickets_hasErrors_backToWedstrijdView() {
        //given
        Map<UUID, Stadion> stadions = (Map<UUID, Stadion>) ReflectionTestUtils.getField(InMemDB.DB, "stadions");
        Stadion stadion = stadions.values().stream().findFirst().get();
        Wedstrijd wedstrijd = stadionDao.getByNaam(stadion.getNaam()).getWedstrijden().get(0);

        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        ArgumentCaptor<String> fieldCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> errorCodeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> defaultMessageCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(bindingResult).rejectValue(fieldCaptor.capture(), errorCodeCaptor.capture(), defaultMessageCaptor.capture());
        when(bindingResult.hasErrors()).thenReturn(true);

        String email = "NotOK";
        TicketsCommand ticketsCommand = new TicketsCommand();
        ticketsCommand.setEmail(email);
        ticketsCommand.setAantal("-5");
        ticketsCommand.setVoetbalCode1("30");
        ticketsCommand.setVoetbalCode2("10");

        int aantalBeschikbareTicketsVoorTest = wedstrijd.getAantalBeschikbarePlaatsen();

        //when
        var result = subject.bestelTickets(wedstrijd.getId().toString(), ticketsCommand, bindingResult, Mockito.mock(Model.class));

        //then
        assertThat(result).isEqualTo("wedstrijdView");
        assertThat(wedstrijdDao.get(wedstrijd.getId()).getAantalBeschikbarePlaatsen()).isEqualTo(aantalBeschikbareTicketsVoorTest);

        List<Map<String, Object>> dbResults = jdbcTemplate.queryForList("select * from tickets t where t.email = '" + email + "'", new HashMap<>());
        assertThat(dbResults.size()).isEqualTo(0);

        assertThat(fieldCaptor.getAllValues()).containsExactlyInAnyOrder("voetbalCode1", "aantal", "email");
        assertThat(errorCodeCaptor.getAllValues()).containsExactlyInAnyOrder("voetbalCode1.kleinerDan", "aantalTickets.kleiner", "email.at");
        assertThat(defaultMessageCaptor.getAllValues()).containsExactlyInAnyOrder("voetbalCode1 moet kleiner zijn dan voetbalCode2",
                "aantal tickets moet groter of gelijk zijn aan 1",
                "geen geldig email");

    }

    @Test
    public void testBestelTickets_hasErrors2_backToWedstrijdView() {
        //given
        Map<UUID, Stadion> stadions = (Map<UUID, Stadion>) ReflectionTestUtils.getField(InMemDB.DB, "stadions");
        Stadion stadion = stadions.values().stream().findFirst().get();
        Wedstrijd wedstrijd = stadionDao.getByNaam(stadion.getNaam()).getWedstrijden().get(0);

        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        ArgumentCaptor<String> fieldCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> errorCodeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> defaultMessageCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(bindingResult).rejectValue(fieldCaptor.capture(), errorCodeCaptor.capture(), defaultMessageCaptor.capture());
        when(bindingResult.hasErrors()).thenReturn(true);

        String email = "email@is.ok";
        TicketsCommand ticketsCommand = new TicketsCommand();
        ticketsCommand.setEmail(email);
        ticketsCommand.setAantal("30");
        ticketsCommand.setVoetbalCode1("NAN");
        ticketsCommand.setVoetbalCode2("NAN2");

        int aantalBeschikbareTicketsVoorTest = wedstrijd.getAantalBeschikbarePlaatsen();

        //when
        var result = subject.bestelTickets(wedstrijd.getId().toString(), ticketsCommand, bindingResult, Mockito.mock(Model.class));

        //then
        assertThat(result).isEqualTo("wedstrijdView");
        assertThat(wedstrijdDao.get(wedstrijd.getId()).getAantalBeschikbarePlaatsen()).isEqualTo(aantalBeschikbareTicketsVoorTest);

        List<Map<String, Object>> dbResults = jdbcTemplate.queryForList("select * from tickets t where t.email = '" + email + "'", new HashMap<>());
        assertThat(dbResults.size()).isEqualTo(0);

        assertThat(fieldCaptor.getAllValues()).containsExactlyInAnyOrder("voetbalCode1", "voetbalCode2", "aantal");
        assertThat(errorCodeCaptor.getAllValues()).containsExactlyInAnyOrder("voetbalCode1.getal", "voetbalCode2.getal", "aantalTickets.groter");
        assertThat(defaultMessageCaptor.getAllValues()).containsExactlyInAnyOrder("moet uit getallen bestaan",
                "moet uit getallen bestaan",
                "aantal tickets moet kleiner of gelijk zijn aan 25");

    }

}
