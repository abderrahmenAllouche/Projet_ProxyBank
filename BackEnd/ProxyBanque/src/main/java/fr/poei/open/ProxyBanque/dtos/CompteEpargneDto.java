package fr.poei.open.proxybanque.dtos;

import java.time.LocalDate;

import fr.poei.open.proxybanque.entities.Client;

public class CompteEpargneDto {

    private Long id;
    private Client client;
    private String numCompte;
    private Long solde;
    private LocalDate date;
    private Float taux;




    public CompteEpargneDto(Long id, Client client, String numCompte, Long solde, LocalDate date, Float taux) {
        super();
        this.id = id;
        this.client = client;
        this.numCompte = numCompte;
        this.solde = solde;
        this.date = date;
        this.taux = taux;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getNumCompte() {
        return numCompte;
    }

    public void setNumCompte(String numCompte) {
        this.numCompte = numCompte;
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

    public Float getTaux() {
        return taux;
    }

    public void setTaux(Float taux) {
        this.taux = taux;
    }

    @Override
    public String toString() {
        return "CompteEpargneDto [id=" + id + ", client=" + client + ", numCompte=" + numCompte + ", solde=" + solde
                + ", date=" + date + ", taux=" + taux + "]";
    }

}
