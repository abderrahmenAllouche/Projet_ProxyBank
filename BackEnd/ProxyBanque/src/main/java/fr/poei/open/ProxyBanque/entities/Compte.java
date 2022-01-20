package fr.poei.open.ProxyBanque.entities;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Compte {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Cascade sert à corriger l'exception :TransientObjectException
    @OneToOne(cascade = CascadeType.ALL)
    private Client client;

    private LocalDate date;
    private Long solde;
    private String typeCompte;

    public Compte() {
        super();
    }

    public Compte(Client client) {
        super();

        this.client = client;

    }

    public Compte(Client client, Long solde) {
        super();

        this.client = client;
        this.solde = solde;
        this.date = date.now();

    }


    public Compte(Long solde) {
        super();
        this.solde = solde;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSolde() {
        return solde;
    }

    public void setSolde(Long solde) {
        this.solde = solde;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    public String getTypeCompte() {
        return typeCompte;
    }



    @Override
    public String toString() {
        return "Compte [id=" + id + ", client=" + client + ", solde=" + solde + ", date=" + date + "]";
    }

}
