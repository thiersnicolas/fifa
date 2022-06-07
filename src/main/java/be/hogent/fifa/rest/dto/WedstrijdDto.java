package be.hogent.fifa.rest.dto;

import be.hogent.fifa.domain.Wedstrijd;

import java.time.ZonedDateTime;
import java.util.UUID;

public class WedstrijdDto {
    private final UUID id;
    private final String land1;
    private final String land2;
    private final ZonedDateTime tijdstip;
    private final UUID stadionId;
    private final String stadionNaam;
    private final int aantalBeschikbarePlaatsen;

    private WedstrijdDto(UUID id, String land1, String land2, ZonedDateTime tijdstip, UUID stadionId, String stadionNaam, int aantalBeschikbarePlaatsen) {
        this.id = id;
        this.land1 = land1;
        this.land2 = land2;
        this.tijdstip = tijdstip;
        this.stadionId = stadionId;
        this.stadionNaam = stadionNaam;
        this.aantalBeschikbarePlaatsen = aantalBeschikbarePlaatsen;
    }

    public static WedstrijdDto fromWedstrijd(Wedstrijd wedstrijd) {
        return new WedstrijdDto(wedstrijd.getId(), wedstrijd.getLand1(), wedstrijd.getLand2(), wedstrijd.getTijdstip(),
                wedstrijd.getStadion().getId(), wedstrijd.getStadion().getNaam(), wedstrijd.getAantalBeschikbarePlaatsen());
    }

    public UUID getId() {
        return id;
    }

    public String getLand1() {
        return land1;
    }

    public String getLand2() {
        return land2;
    }

    public ZonedDateTime getTijdstip() {
        return tijdstip;
    }

    public UUID getStadionId() {
        return stadionId;
    }

    public String getStadionNaam() {
        return stadionNaam;
    }

    public int getAantalBeschikbarePlaatsen() {
        return aantalBeschikbarePlaatsen;
    }
}
