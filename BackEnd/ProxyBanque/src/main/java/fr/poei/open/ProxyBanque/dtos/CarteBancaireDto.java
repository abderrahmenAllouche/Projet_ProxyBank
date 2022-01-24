package fr.poei.open.proxybanque.dtos;

import fr.poei.open.proxybanque.entities.Client;

public class CarteBancaireDto {

    private Long id;
    private Client client;

    private String typeCarte;

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

    public String getTypeCarte() {
        return typeCarte;
    }

    public void setTypeCarte(String typeCarte) {
        this.typeCarte = typeCarte;
    }

    @Override
    public String toString() {

        System.out.println();
        return "CarteBancaire [id=" + id + "]";
    }

}
