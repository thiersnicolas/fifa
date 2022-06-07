package be.hogent.fifa.repositories;

import be.hogent.fifa.domain.Wedstrijd;

import java.util.List;
import java.util.UUID;

public interface WedstrijdDao extends GenericDao<Wedstrijd> {
    List<Wedstrijd> findByIds(List<UUID> ids);
}