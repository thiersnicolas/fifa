package be.hogent.fifa.repositories;

import be.hogent.fifa.domain.WedstrijdTicket;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

public class JpaTicketDao extends GenericDaoJpa<WedstrijdTicket> implements WedstrijdTicketDao {
    public JpaTicketDao() {
        super(WedstrijdTicket.class);
    }
}
