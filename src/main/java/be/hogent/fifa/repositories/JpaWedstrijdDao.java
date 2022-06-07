package be.hogent.fifa.repositories;

import be.hogent.fifa.domain.Wedstrijd;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

public class JpaWedstrijdDao extends GenericDaoJpa<Wedstrijd> implements WedstrijdDao {
    public JpaWedstrijdDao() {
        super(Wedstrijd.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Wedstrijd> findByIds(List<UUID> ids) {
        return em.createNamedQuery(
                "Wedstrijd.getWedstrijdenByIds",
                Wedstrijd.class).setParameter("ids", ids).getResultList();

    }

    @Transactional(readOnly = true)
    @Override
    public Wedstrijd get(UUID id) {
        TypedQuery<Wedstrijd> query = em.createQuery("SELECT w FROM Wedstrijd w WHERE w.id = :id", Wedstrijd.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }
}
