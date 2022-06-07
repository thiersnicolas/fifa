package be.hogent.fifa.repositories;

import java.util.List;
import java.util.UUID;

public interface GenericDao<T> {

    List<T> findAll();

    T update(T object);

    T get(UUID id);

    void delete(T object);

    void insert(T object);

    boolean exists(UUID id);
}