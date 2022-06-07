package be.hogent.fifa.repositories.inmemory;

import be.hogent.fifa.domain.Wedstrijd;
import be.hogent.fifa.repositories.WedstrijdDao;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class InMemWedstrijdDao implements WedstrijdDao {
    @Override
    public List<Wedstrijd> findAll() {
        return new ArrayList<>(InMemDB.DB.getWedstrijden().values());
    }

    @Override
    public Wedstrijd update(Wedstrijd wedstrijd) {
        InMemDB.DB.getWedstrijden().put(wedstrijd.getId(), wedstrijd);
        return wedstrijd;
    }

    @Override
    public Wedstrijd get(UUID id) {
        return InMemDB.DB.getWedstrijden().get(id);
    }

    @Override
    public void delete(Wedstrijd wedstrijd) {
        InMemDB.DB.getWedstrijden().remove(wedstrijd.getId());
    }

    @Override
    public void insert(Wedstrijd wedstrijd) {
        InMemDB.DB.getWedstrijden().put(wedstrijd.getId(), wedstrijd);
    }

    @Override
    public boolean exists(UUID id) {
        return InMemDB.DB.getWedstrijden().containsKey(id);
    }

    @Override
    public List<Wedstrijd> findByIds(List<UUID> ids) {
        return InMemDB.DB.getWedstrijden().values().stream()
                .filter(wedstrijd -> ids.contains(wedstrijd.getId()))
                .collect(Collectors.toList());
    }
}
