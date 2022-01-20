package fr.poei.open.ProxyBanque.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Conseiller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @OneToMany(mappedBy = "conseiller")
    @JsonIgnore
    private List<Client> clients =new ArrayList<>();

    @ManyToOne
    @JsonIgnore
    private Gerant gerant;

    public Conseiller() {
    }

    public Conseiller(String nom, List<Client> clients, Gerant gerant) {
        this.nom = nom;
        this.clients = clients;
        this.gerant = gerant;
    }

    public Conseiller(String nom, Gerant gerant) {
        this.nom = nom;
        this.gerant = gerant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public Gerant getGerant() {
        return gerant;
    }

    public void setGerant(Gerant gerant) {
        this.gerant = gerant;
    }

    @Override
    public String toString() {
        return "Conseiller{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", clients=" + clients +
                ", gerant=" + gerant +
                '}';
    }
}
