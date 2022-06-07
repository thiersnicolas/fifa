package be.hogent.fifa.repositories.inmemory;

import be.hogent.fifa.domain.WedstrijdTicket;
import be.hogent.fifa.repositories.WedstrijdTicketDao;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InMemWedstrijdTicketDao implements WedstrijdTicketDao {
    @Override
    public List<WedstrijdTicket> findAll() {
        return new ArrayList<>(InMemDB.DB.getTickets().values());
    }

    @Override
    public WedstrijdTicket update(WedstrijdTicket wedstrijdTicket) {
        InMemDB.DB.getTickets().put(wedstrijdTicket.getId(), wedstrijdTicket);
        return wedstrijdTicket;
    }

    @Override
    public WedstrijdTicket get(UUID id) {
        return InMemDB.DB.getTickets().get(id);
    }

    @Override
    public void delete(WedstrijdTicket wedstrijdTicket) {
        InMemDB.DB.getTickets().remove(wedstrijdTicket.getId());
    }

    @Override
    public void insert(WedstrijdTicket wedstrijdTicket) {
        InMemDB.DB.getTickets().put(wedstrijdTicket.getId(), wedstrijdTicket);
    }

    @Override
    public boolean exists(UUID id) {
        return InMemDB.DB.getTickets().containsKey(id);
    }
}
