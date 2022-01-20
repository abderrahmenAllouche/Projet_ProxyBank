package fr.poei.open.ProxyBanque.entities;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity

public class Agence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private LocalDate dateCreation;
    private String numeroIdentification;

    @OneToOne
    private Gerant gerant;
    private String nom;


    public Agence() {
        super();
    }

    public Agence(Gerant gerant, String nom) {
        super();
        this.gerant = gerant;
        this.nom = nom;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getNumeroIdentification() {
        return numeroIdentification;
    }

    public void setNumeroIdentification(String numeroIdentification) {
        this.numeroIdentification = Integer.toString(this.id) + Integer.toString(this.dateCreation.getDayOfMonth() + this.dateCreation.getMonthValue() + this.dateCreation.getYear());
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Gerant getGerant() {
        return gerant;
    }

    public void setGerant(Gerant gerant) {
        this.gerant = gerant;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


}
