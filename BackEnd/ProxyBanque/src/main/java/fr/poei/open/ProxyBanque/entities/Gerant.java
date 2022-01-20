package fr.poei.open.ProxyBanque.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Gerant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;

    @OneToMany(mappedBy = "gerant")
    private List<Conseiller> Conseillers = new ArrayList<>();

    public Gerant(String nom, List<Conseiller> conseillers) {
        super();
        this.nom = nom;
        this.Conseillers = conseillers;
    }

    public Gerant() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Conseiller> getConseillers() {
        return Conseillers;
    }

    public void setConseillers(List<Conseiller> conseillers) {
        this.Conseillers = conseillers;
    }

    @Override
    public String toString() {
        return "Gerant [id=" + id + ", nom=" + nom + ", Conseillers=" + Conseillers + "]";
    }


}


