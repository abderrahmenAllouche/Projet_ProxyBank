package fr.poei.open.proxybanque.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class CarteVisa  extends CarteBancaire  {

	@JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private Client client;

    private String typeCarte = "carte_Visa";

    public CarteVisa() {
    	
        super();
    }

    public CarteVisa(String typeCarte, Client client) {
    
        this.client = client;
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

    @Override
    public String toString() {
        return "CarteVisa [client=" + client + ", typeCarte=" + typeCarte + "]";
    }




}
