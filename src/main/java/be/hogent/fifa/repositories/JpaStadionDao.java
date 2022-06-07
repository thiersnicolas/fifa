package be.hogent.fifa.repositories;

import be.hogent.fifa.domain.Stadion;
import be.hogent.fifa.domain.Wedstrijd;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public class JpaStadionDao extends GenericDaoJpa<Stadion> implements StadionDao {
    public JpaStadionDao() {
        super(Stadion.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Stadion getByNaam(String naam) {
        return em.createNamedQuery(
                        "Stadion.getStadionByNaam",
                        Stadion.class).setParameter("naam", naam).getResultList().stream()
                .findAny().orElse(null);

    }
}
