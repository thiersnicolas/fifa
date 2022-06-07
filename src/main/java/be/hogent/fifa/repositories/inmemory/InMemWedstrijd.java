package be.hogent.fifa.repositories.inmemory;

import be.hogent.fifa.domain.Stadion;
import be.hogent.fifa.domain.Wedstrijd;

import java.time.ZonedDateTime;
import java.util.UUID;

public class InMemWedstrijd extends Wedstrijd {
    public InMemWedstrijd(String land1, String land2, ZonedDateTime tijdstip, Stadion stadion, int aantalBeschikbarePlaatsen) {
        super(UUID.randomUUID(), land1, land2, tijdstip, stadion, aantalBeschikbarePlaatsen);
    }

    @Override
    public int getAantalBeschikbarePlaatsen() {
        return super.getStadion().getAantalPlaatsen() - Long.valueOf(InMemDB.DB.getTickets().values().stream()
                .filter(wedstrijdTicket -> wedstrijdTicket.getWedstrijd().getId().equals(this.getId()))
                .count()).intValue();
    }
}
