package fr.poei.open.proxybanque.dtos;

import java.time.LocalDate;

import fr.poei.open.proxybanque.entities.Gerant;

public class AgenceDtos {
    private Integer id;
    private LocalDate dateCreation;
    private String numeroIdentification;
    private Gerant gerant;
    private String nom;

    public AgenceDtos(Integer id, LocalDate dateCreation, String numeroIdentification, Gerant gerant, String nom) {
        super();
        this.id = id;
        this.dateCreation = dateCreation.now();
        this.numeroIdentification = Integer.toString(id) + Integer.toString(dateCreation.getDayOfMonth() + dateCreation.getMonthValue() + dateCreation.getYear());
        this.gerant = gerant;
        this.nom = nom;
    }

    public AgenceDtos() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        this.numeroIdentification = numeroIdentification;
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
