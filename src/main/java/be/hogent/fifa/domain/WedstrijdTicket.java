package be.hogent.fifa.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tickets")
public class WedstrijdTicket {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID id;
    @Column
    private String email;
    @Column
    private int voetbalCode1;
    @Column
    private int voetbalCode2;
    @ManyToOne
    private Wedstrijd wedstrijd;

    protected WedstrijdTicket() {
    }

    public WedstrijdTicket(String email, int voetbalCode1, int voetbalCode2, Wedstrijd wedstrijd) {
        this.email = email;
        this.voetbalCode1 = voetbalCode1;
        this.voetbalCode2 = voetbalCode2;
        this.wedstrijd = wedstrijd;
    }

    protected WedstrijdTicket(UUID id, String email, int voetbalCode1, int voetbalCode2, Wedstrijd wedstrijd) {
        this.id = id;
        this.email = email;
        this.voetbalCode1 = voetbalCode1;
        this.voetbalCode2 = voetbalCode2;
        this.wedstrijd = wedstrijd;
    }

    public static WedstrijdTicket withUUID(String email, int voetbalCode1, int voetbalCode2, Wedstrijd wedstrijd) {
        return new WedstrijdTicket(UUID.randomUUID(), email, voetbalCode1, voetbalCode2, wedstrijd);
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public int getVoetbalCode1() {
        return voetbalCode1;
    }

    public int getVoetbalCode2() {
        return voetbalCode2;
    }

    public Wedstrijd getWedstrijd() {
        return wedstrijd;
    }
}
