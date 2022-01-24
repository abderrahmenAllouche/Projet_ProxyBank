package fr.poei.open.proxybanque.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class CarteElectron extends CarteBancaire {

    @OneToOne(cascade = CascadeType.ALL)
    private Client client;

    private String typeCarte = "carte_Electron";
    private String numCarte;

    public CarteElectron() {
        super();
    }

    public CarteElectron(String typeCarte, Client client) {
        super(client);
        this.typeCarte = typeCarte;
    }

    public String getTypeCarte() {
        return typeCarte;
    }

    public void setTypeCarte(String typeCarte) {
        this.typeCarte = typeCarte;
    }

    public Client getClient() {
        return client;
    }

    public String getNumCarte() {
        return numCarte;
    }

    public void setNumCarte(String numCarte) {
        this.numCarte = numCarte;
    }

    @Override
    public String toString() {
        return "CarteElectron [client=" + client + ", typeCarte=" + typeCarte + ", numCarte=" + numCarte + "]";
    }





}
