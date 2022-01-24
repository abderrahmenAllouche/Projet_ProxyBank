package fr.poei.open.proxybanque.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class CarteVisa  extends CarteBancaire  {

    @OneToOne(cascade = CascadeType.ALL)
    private Client client;

    private String typeCarte = "carte_Visa";
    private String numCarte;

    public CarteVisa() {
        super();
    }

    public CarteVisa(String typeCarte, Client client) {
        super(client);
        this.typeCarte = typeCarte;
    }

    public String getTypeCarte() {
        return typeCarte;
    }

    public void setTypeCarte(String typeCarte) {
        this.typeCarte = typeCarte;
    }

    public String getNumCarte() {
        return numCarte;
    }

    public void setNumCarte(String numCompte) {
        this.numCarte = numCompte;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public String toString() {
        return "CarteVisa [client=" + client + ", typeCarte=" + typeCarte + ", numCarte=" + numCarte + "]";
    }




}
