package fr.poei.open.proxybanque.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
public class Gerant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    @OneToOne
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "gerant")
    private List<Conseiller> Conseillers = new ArrayList<>();

    public Gerant(String nom, List<Conseiller> conseillers, Utilisateur utilisateur) {
        super();
        this.nom = nom;
        this.Conseillers = conseillers;
        this.utilisateur = utilisateur;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
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


