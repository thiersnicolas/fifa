package be.hogent.fifa.repositories;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

@Transactional
public class GenericDaoJpa<T> implements GenericDao<T> {
    private final Class<T> type;
    protected EntityManager em;

    public GenericDaoJpa(Class<T> type) {
        super();
        this.type = type;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Transactional(readOnly = true)
    public T get(UUID id) {
        return this.em.find(this.type, id);
    }

    @Transactional(readOnly = true)
    public List<T> findAll() {
        return this.em.createQuery("select entity from " + this.type.getName() + " entity").getResultList();
    }

    @Override
    public void insert(T object) {
        em.persist(object);
    }

    @Override
    public void delete(T object) {
        em.remove(em.merge(object));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean exists(UUID id) {
        T entity = this.em.find(this.type, id);
        return entity != null;
    }

    @Override
    public T update(T object) {
        return em.merge(object);
    }
}