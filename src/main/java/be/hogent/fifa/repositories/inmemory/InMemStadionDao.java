package be.hogent.fifa.repositories.inmemory;

import be.hogent.fifa.domain.Stadion;
import be.hogent.fifa.repositories.StadionDao;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InMemStadionDao implements StadionDao {
    @Override
    public List<Stadion> findAll() {
        return new ArrayList<>(InMemDB.DB.getStadions().values());
    }

    @Override
    public Stadion update(Stadion stadion) {
        InMemDB.DB.getStadions().put(stadion.getId(), stadion);
        return stadion;
    }

    @Override
    public Stadion get(UUID id) {
        return InMemDB.DB.getStadions().get(id);
    }

    @Override
    public void delete(Stadion stadion) {
        InMemDB.DB.getStadions().remove(stadion.getId());
    }

    @Override
    public void insert(Stadion stadion) {
        InMemDB.DB.getStadions().put(stadion.getId(), stadion);
    }

    @Override
    public boolean exists(UUID id) {
        return InMemDB.DB.getStadions().containsKey(id);
    }

    @Override
    public Stadion getByNaam(String naam) {
        return InMemDB.DB.getStadions().values().stream()
                .filter(st -> st.getNaam().equals(naam))
                .findAny().orElse(null);
    }
}
