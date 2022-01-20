package fr.poei.open.ProxyBanque.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class CompteCourant extends Compte {

    // Cascade sert à corriger l'exception :TransientObjectException
    @OneToOne(cascade = CascadeType.ALL)
    private Client client;

    private String numCompte;
    private Long autorisDecouvert = 0L ;
    private String typeCompte = "compte_Courant";

    public String getTypeCompte() {
        return typeCompte;
    }

    public CompteCourant() {
        super();
    }

    public CompteCourant(Client client, Long solde) {
        super(client, solde);
        // this.autorisDecouvert = 100F;

    }

    public CompteCourant(Client client) {
        super(client);

        // this.autorisDecouvert = 100F;

    }

    public String getNumCompte() {
        return numCompte;
    }

    public void setNumCompte(String numCompte) {
        this.numCompte = numCompte;
    }

    public Long getAutorisDecouvert() {
        return autorisDecouvert;
    }

    public void setAutorisDecouvert(Long autorisDecouvert) {
        this.autorisDecouvert = autorisDecouvert;
    }

    @Override
    public String toString() {
        return "CompteCourant [autorisation decouvert=" + autorisDecouvert + "]";
    }

}
