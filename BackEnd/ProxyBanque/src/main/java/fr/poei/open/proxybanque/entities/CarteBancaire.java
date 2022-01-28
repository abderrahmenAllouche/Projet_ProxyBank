package fr.poei.open.proxybanque.entities;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class CarteBancaire {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String typeCarte;
    
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private Client client;

    private String numCarte;
    
    public CarteBancaire() {
        super();
    }

    public CarteBancaire(Client client) {
        super();
        this.numCarte=  Long.toString(client.getId()) + Integer.toString(LocalDate.now().getYear())
		+ Integer.toString(LocalDate.now().getMonthValue()) + Integer.toString(LocalDate.now().getDayOfMonth());
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setClient(Client client) {
        this.client = client;
    }

    
    
    public String getNumCarte() {
		return numCarte;
	}

	public void setNumCarte(String numCarte) {
		this.numCarte = numCarte;
	}

	@Override
	public String toString() {
		return "CarteBancaire [id=" + id + ", typeCarte=" + typeCarte + ", client=" + client + ", numCarte=" + numCarte
				+ "]";
	}


}
