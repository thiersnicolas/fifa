package be.hogent.fifa.repositories.inmemory;

import be.hogent.fifa.domain.Stadion;
import be.hogent.fifa.domain.Wedstrijd;
import be.hogent.fifa.domain.WedstrijdTicket;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public enum InMemDB {
    DB;
    private final Map<UUID, Stadion> stadions;
    private final Map<UUID, Wedstrijd> wedstrijden;
    private final Map<UUID, WedstrijdTicket> tickets;

    InMemDB() {
        stadions = new HashMap<>();
        Stadion stadionAlBayt = Stadion.withUUID("Al Bayt Stadium", 5);
        Stadion stadionAlThumamaStadion = Stadion.withUUID("Al Thumama Stadium", 40000);
        List.of(stadionAlBayt, stadionAlThumamaStadion)
                .forEach(stadion -> stadions.put(stadion.getId(), stadion));

        wedstrijden = new HashMap<>();
        Wedstrijd wedstrijdBelgieCanada = new InMemWedstrijd("België", "Canada",
                ZonedDateTime.of(2022, 11, 26, 9, 0, 0, 0,
                        ZoneId.of("CET")), stadionAlBayt, stadionAlBayt.getAantalPlaatsen());
        Wedstrijd wedstrijdBrazilieZwitserland = new InMemWedstrijd("Brazilië", "Zwitserland",
                ZonedDateTime.of(2022, 11, 27, 9, 0, 0, 0,
                        ZoneId.of("CET")), stadionAlThumamaStadion, stadionAlThumamaStadion.getAantalPlaatsen());
        Wedstrijd wedstrijdMarrokoKroatie = new InMemWedstrijd("Marroko", "Kroatië",
                ZonedDateTime.of(2022, 11, 28, 9, 0, 0, 0,
                        ZoneId.of("CET")), stadionAlBayt, stadionAlBayt.getAantalPlaatsen());
        Wedstrijd wedstrijdSpanjeDuitsland = new InMemWedstrijd("Spanje", "Duitsland",
                ZonedDateTime.of(2022, 11, 28, 9, 0, 0, 0,
                        ZoneId.of("CET")), stadionAlThumamaStadion, stadionAlThumamaStadion.getAantalPlaatsen());
        Wedstrijd wedstrijdFrankrijkDenemarken = new InMemWedstrijd("Frankrijk", "Denemarken",
                ZonedDateTime.of(2022, 11, 30, 9, 0, 0, 0,
                        ZoneId.of("CET")), stadionAlBayt, stadionAlBayt.getAantalPlaatsen());
        Wedstrijd wedstrijdArgentinieMexico = new InMemWedstrijd("Argentinië", "Mexico",
                ZonedDateTime.of(2022, 11, 30, 9, 0, 0, 0,
                        ZoneId.of("CET")), stadionAlThumamaStadion, stadionAlThumamaStadion.getAantalPlaatsen());
        Wedstrijd wedstrijdEngelandUSA = new InMemWedstrijd("Engeland", "USA",
                ZonedDateTime.of(2022, 12, 1, 9, 0, 0, 0,
                        ZoneId.of("CET")), stadionAlBayt, stadionAlBayt.getAantalPlaatsen());
        Wedstrijd wedstrijdNederlandQatar = new InMemWedstrijd("Nederland", "Qatar",
                ZonedDateTime.of(2022, 12, 1, 9, 0, 0, 0,
                        ZoneId.of("CET")), stadionAlThumamaStadion, stadionAlThumamaStadion.getAantalPlaatsen());

        List.of(wedstrijdBelgieCanada, wedstrijdBrazilieZwitserland, wedstrijdMarrokoKroatie, wedstrijdSpanjeDuitsland,
                        wedstrijdFrankrijkDenemarken, wedstrijdArgentinieMexico, wedstrijdEngelandUSA, wedstrijdNederlandQatar)
                .forEach(wedstrijd -> {
                    wedstrijden.put(wedstrijd.getId(), wedstrijd);
                    wedstrijd.getStadion().getWedstrijden().add(wedstrijd);
                });

        tickets = new HashMap<>();
        List.of(WedstrijdTicket.withUUID("demo@demo.com", 35, 45, wedstrijdBelgieCanada),
                        WedstrijdTicket.withUUID("demo@demo.com", 35, 45, wedstrijdBrazilieZwitserland),
                        WedstrijdTicket.withUUID("demo@demo.com", 35, 45, wedstrijdMarrokoKroatie),
                        WedstrijdTicket.withUUID("demo@demo.com", 35, 45, wedstrijdSpanjeDuitsland),
                        WedstrijdTicket.withUUID("demo@demo.com", 35, 45, wedstrijdFrankrijkDenemarken),
                        WedstrijdTicket.withUUID("demo@demo.com", 35, 45, wedstrijdArgentinieMexico),
                        WedstrijdTicket.withUUID("demo@demo.com", 35, 45, wedstrijdEngelandUSA),
                        WedstrijdTicket.withUUID("demo@demo.com", 35, 45, wedstrijdNederlandQatar))
                .forEach(wedstrijdTicket -> tickets.put(wedstrijdTicket.getId(), wedstrijdTicket));
    }

    protected Map<UUID, Stadion> getStadions() {
        return stadions;
    }

    protected Map<UUID, Wedstrijd> getWedstrijden() {
        return wedstrijden;
    }

    protected Map<UUID, WedstrijdTicket> getTickets() {
        return tickets;
    }
}
