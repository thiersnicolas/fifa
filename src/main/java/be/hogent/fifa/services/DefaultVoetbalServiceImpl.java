package be.hogent.fifa.services;

import be.hogent.fifa.domain.Stadion;
import be.hogent.fifa.domain.Wedstrijd;
import be.hogent.fifa.domain.WedstrijdTicket;
import be.hogent.fifa.repositories.StadionDao;
import be.hogent.fifa.repositories.WedstrijdDao;
import be.hogent.fifa.repositories.WedstrijdTicketDao;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DefaultVoetbalServiceImpl implements VoetbalService {
    private final StadionDao stadionDao;
    private final WedstrijdDao wedstrijdDao;
    private final WedstrijdTicketDao wedstrijdTicketDao;

    public DefaultVoetbalServiceImpl(StadionDao stadionDao, WedstrijdDao wedstrijdDao, WedstrijdTicketDao wedstrijdTicketDao) {
        this.stadionDao = stadionDao;
        this.wedstrijdDao = wedstrijdDao;
        this.wedstrijdTicketDao = wedstrijdTicketDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stadion> getStadionLijst() {
        return stadionDao.findAll();
    }

    @Override
    public List<Wedstrijd> getWedstrijdLijst() {
        return wedstrijdDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Wedstrijd> getWedstrijdenVoorStadion(UUID stadionId) {
        return new ArrayList<>(stadionDao.get(stadionId).getWedstrijden());
    }

    @Override
    public List<Wedstrijd> getWedstrijdenVoorStadionNaam(String stadionNaam) {
        return new ArrayList<>(stadionDao.getByNaam(stadionNaam).getWedstrijden());
    }

    @Override
    @Transactional(readOnly = true)
    public WedstrijdTicket getWedstrijdTicket(UUID id) {
        return wedstrijdTicketDao.get(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Wedstrijd getWedstrijd(UUID id) {
        return wedstrijdDao.get(id);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<WedstrijdTicket> ticketsBestellen(List<WedstrijdTicket> teBestellenTickets) {
        Map<Wedstrijd, List<WedstrijdTicket>> ticketsPerWedstrijd = teBestellenTickets.stream()
                .collect(Collectors.groupingBy(WedstrijdTicket::getWedstrijd));
        List<Wedstrijd> wedstrijden = wedstrijdDao.findByIds(ticketsPerWedstrijd.keySet().stream()
                .map(Wedstrijd::getId).collect(Collectors.toList()));
        return teBestellenTickets.stream()
                .collect(Collectors.groupingBy(WedstrijdTicket::getWedstrijd))
                .entrySet().stream()
                .filter(ticketsPerWedstrijdEntry -> wedstrijden.contains(ticketsPerWedstrijdEntry.getKey()))
                .flatMap(ticketsPerWedstrijdEntry -> {
                    wedstrijdDao.update(ticketsPerWedstrijdEntry.getKey()
                            .verminderAantalBeschikbarePlaatsen(ticketsPerWedstrijdEntry.getKey()
                                    .getAantalBeschikbareTickets(ticketsPerWedstrijdEntry.getValue().size())));
                    return IntStream.range(0, ticketsPerWedstrijdEntry.getKey()
                                    .getAantalBeschikbareTickets(ticketsPerWedstrijdEntry.getValue().size()))
                            .mapToObj(i -> {
                                wedstrijdTicketDao.insert(ticketsPerWedstrijdEntry.getValue().get(i));
                                return ticketsPerWedstrijdEntry.getValue().get(i);
                            });
                }).collect(Collectors.toList());
    }
}
