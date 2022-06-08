package be.hogent.fifa.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "wedstrijden")
@NamedQueries({
        @NamedQuery(name =
                "Wedstrijd.getWedstrijdenByIds",
                query = "SELECT w FROM Wedstrijd w WHERE "
                        + "w.id in :ids")
})
public class Wedstrijd {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID id;
    @Column
    private String land1;
    @Column
    private String land2;
    @Column
    private ZonedDateTime tijdstip;
    @ManyToOne
    @JoinColumn(name = "stadion_id", nullable = false)
    private Stadion stadion;
    @Column
    private int aantalBeschikbarePlaatsen;

    protected Wedstrijd() {
    }

    public Wedstrijd(String land1, String land2, ZonedDateTime tijdstip, Stadion stadion, int aantalBeschikbarePlaatsen) {
        this.land1 = land1;
        this.land2 = land2;
        this.tijdstip = tijdstip;
        this.stadion = stadion;
        this.aantalBeschikbarePlaatsen = aantalBeschikbarePlaatsen;
    }

    //voor inmemory
    protected Wedstrijd(UUID id, String land1, String land2, ZonedDateTime tijdstip, Stadion stadion, int aantalBeschikbarePlaatsen) {
        this.id = id;
        this.land1 = land1;
        this.land2 = land2;
        this.tijdstip = tijdstip;
        this.stadion = stadion;
        this.aantalBeschikbarePlaatsen = aantalBeschikbarePlaatsen;
    }

    public static Wedstrijd withUUID(String land1, String land2, ZonedDateTime tijdstip, Stadion stadion, int aantalBeschikbarePlaatsen) {
        return new Wedstrijd(UUID.randomUUID(), land1, land2, tijdstip, stadion, aantalBeschikbarePlaatsen);
    }

    public int getAantalBeschikbareTickets(int aantal) {
        int beschikbareTickets = getAantalBeschikbarePlaatsen();
        if (aantal <= 0) {
            return -1;
        } else return Math.min(beschikbareTickets, aantal);
    }

    public boolean uitverkocht() {
        return getAantalBeschikbarePlaatsen() == 0;
    }

    public List<String> getLanden() {
        return List.of(land1, land2);
    }

    public UUID getId() {
        return id;
    }

    public String getLand1() {
        return land1;
    }

    public String getLand2() {
        return land2;
    }

    public ZonedDateTime getTijdstip() {
        return tijdstip;
    }

    public Stadion getStadion() {
        return stadion;
    }

    public int getAantalBeschikbarePlaatsen() {
        return aantalBeschikbarePlaatsen;
    }

    public Wedstrijd verminderAantalBeschikbarePlaatsen(int verkochteTickets) {
        this.aantalBeschikbarePlaatsen -= verkochteTickets;
        return this;
    }

    @Override
    public String toString() {
        return "Wedstrijd{" +
                "id=" + id +
                ", land1='" + land1 + '\'' +
                ", land2='" + land2 + '\'' +
                ", tijdstip=" + tijdstip +
                ", stadion=" + stadion +
                ", aantalBeschikbarePlaatsen=" + aantalBeschikbarePlaatsen +
                '}';
    }

    public String toStringVanStadion() {
        return "Wedstrijd{" +
                "id=" + id +
                ", land1='" + land1 + '\'' +
                ", land2='" + land2 + '\'' +
                ", tijdstip=" + tijdstip +
                ", aantalBeschikbarePlaatsen=" + aantalBeschikbarePlaatsen +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Wedstrijd)) return false;

        Wedstrijd wedstrijd = (Wedstrijd) o;

        if (getAantalBeschikbarePlaatsen() != wedstrijd.getAantalBeschikbarePlaatsen()) return false;
        if (getId() != null ? !getId().equals(wedstrijd.getId()) : wedstrijd.getId() != null) return false;
        if (getLand1() != null ? !getLand1().equals(wedstrijd.getLand1()) : wedstrijd.getLand1() != null) return false;
        if (getLand2() != null ? !getLand2().equals(wedstrijd.getLand2()) : wedstrijd.getLand2() != null) return false;
        if (getTijdstip() != null ? !getTijdstip().equals(wedstrijd.getTijdstip()) : wedstrijd.getTijdstip() != null)
            return false;
        return getStadion() != null ? getStadion().equals(wedstrijd.getStadion()) : wedstrijd.getStadion() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getLand1() != null ? getLand1().hashCode() : 0);
        result = 31 * result + (getLand2() != null ? getLand2().hashCode() : 0);
        result = 31 * result + (getTijdstip() != null ? getTijdstip().hashCode() : 0);
        result = 31 * result + (getStadion() != null ? getStadion().hashCode() : 0);
        result = 31 * result + getAantalBeschikbarePlaatsen();
        return result;
    }
}
