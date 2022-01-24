package fr.poei.open.proxybanque.dtos;

import java.time.LocalDate;

import fr.poei.open.proxybanque.entities.Client;

public class CompteCourantDto {

    private Long id;
    private Client client;
    private String numCompte;
    private Long solde;
    private LocalDate date;
    private Long autorisDecouvert;



    public CompteCourantDto(Long id, Client client, String numCompte, Long solde, LocalDate date,
                            Long autorisDecouvert) {
        super();
        this.id = id;
        this.client = client;
        this.numCompte = numCompte;
        this.solde = solde;
        this.date = date;
        this.autorisDecouvert = autorisDecouvert;

    }



    public CompteCourantDto() {
        super();
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

    public Long getAutorisDecouvert() {
        return autorisDecouvert;
    }

    public void setAutorisDecouvert(Long autorisDecouvert) {
        this.autorisDecouvert = autorisDecouvert;
    }

    @Override
    public String toString() {
        return "CompteCourantDto [id=" + id + ", client=" + client + ", numCompte=" + numCompte + ", solde=" + solde
                + ", date=" + date + ", autorisDecouvert=" + autorisDecouvert + "]";
    }

}
