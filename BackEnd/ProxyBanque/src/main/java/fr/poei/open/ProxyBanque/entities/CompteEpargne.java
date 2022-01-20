package fr.poei.open.ProxyBanque.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class CompteEpargne extends Compte {

    // Cascade sert à corriger l'exception :TransientObjectException
    @OneToOne(cascade = CascadeType.ALL)
    private Client client;

    private String numCompte;
    private Float taux;
    private String typeCompte = "compte_Epargne";

    public String getTypeCompte() {
        return typeCompte;
    }

    public CompteEpargne() {
        super();
    }

    public CompteEpargne(Client client,Long solde, Float taux) {
        super(solde);
        this.taux = taux;
        // TODO Auto-generated constructor stub
    }

    public CompteEpargne(Client client,Float taux) {
        super();
        this.taux = taux;
    }

    public String getNumCompte() {
        return numCompte;
    }

    public void setNumCompte(String numCompte) {
        this.numCompte = numCompte;
    }

    public Float getTaux() {
        return taux;
    }

    public void setTaux(Float taux) {
        this.taux = taux;
    }

    @Override
    public String toString() {
        return "CompteEpargne [taux=" + taux + "]";
    }

}
