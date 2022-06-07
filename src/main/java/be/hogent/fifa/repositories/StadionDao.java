package be.hogent.fifa.repositories;

import be.hogent.fifa.domain.Stadion;

public interface StadionDao extends GenericDao<Stadion> {
    Stadion getByNaam(String naam);
}