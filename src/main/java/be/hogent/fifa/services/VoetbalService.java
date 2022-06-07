package be.hogent.fifa.services;

import be.hogent.fifa.domain.Stadion;
import be.hogent.fifa.domain.Wedstrijd;
import be.hogent.fifa.domain.WedstrijdTicket;

import java.util.List;
import java.util.UUID;

public interface VoetbalService {
    List<Stadion> getStadionLijst();

    List<Wedstrijd> getWedstrijdLijst();

    List<Wedstrijd> getWedstrijdenVoorStadion(UUID stadionId);

    List<Wedstrijd> getWedstrijdenVoorStadionNaam(String stadionNaam);

    WedstrijdTicket getWedstrijdTicket(UUID id);

    Wedstrijd getWedstrijd(UUID id);

    List<WedstrijdTicket> ticketsBestellen(List<WedstrijdTicket> teBestellenTickets);

}