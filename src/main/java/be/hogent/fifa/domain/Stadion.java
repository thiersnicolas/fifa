package be.hogent.fifa.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "stadions")
@NamedQueries({
        @NamedQuery(name =
                "Stadion.getStadionByNaam",
                query = "SELECT s FROM Stadion s LEFT JOIN FETCH s.wedstrijden WHERE s.naam = :naam")
})
public class Stadion {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID id;
    @Column
    private String naam;
    @Column(name = "plaatsen")
    private int aantalPlaatsen;
    @OneToMany(mappedBy="stadion", cascade = CascadeType.ALL)
    private List<Wedstrijd> wedstrijden = new ArrayList<>();

    protected Stadion() {}

    public Stadion(String naam, int aantalPlaatsen) {
        this.naam = naam;
        this.aantalPlaatsen = aantalPlaatsen;
    }

    //voor inmemory
    private Stadion(UUID id, String naam, int aantalPlaatsen) {
        this.id = id;
        this.naam = naam;
        this.aantalPlaatsen = aantalPlaatsen;
    }

    //voor inmemory
    public static Stadion withUUID(String naam, int aantalPlaatsen) {
        return new Stadion(UUID.randomUUID(), naam, aantalPlaatsen);
    }

    public UUID getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public int getAantalPlaatsen() {
        return aantalPlaatsen;
    }

    public List<Wedstrijd> getWedstrijden() {
        return wedstrijden;
    }

    @Override
    public String toString() {
        return "Stadion{" +
                "id=" + id +
                ", naam='" + naam + '\'' +
                ", aantalPlaatsen=" + aantalPlaatsen +
                ", wedstrijden=" + "[" + wedstrijden.stream().map(Wedstrijd::toStringVanStadion).collect(Collectors.joining("; ")) + "]" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stadion)) return false;

        Stadion stadion = (Stadion) o;

        if (getAantalPlaatsen() != stadion.getAantalPlaatsen()) return false;
        if (getId() != null ? !getId().equals(stadion.getId()) : stadion.getId() != null) return false;
        return getNaam() != null ? getNaam().equals(stadion.getNaam()) : stadion.getNaam() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getNaam() != null ? getNaam().hashCode() : 0);
        result = 31 * result + getAantalPlaatsen();
        return result;
    }
}
