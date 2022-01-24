package fr.poei.open.proxybanque.dtos;

import java.util.ArrayList;
import java.util.List;

public class ConseillerDto {

    private Long id;
    private String nom;
    private List<ClientVM> clients =new ArrayList<>();
    private Integer gerant_id;

    public ConseillerDto(String nom, List<ClientVM> clients, Integer gerant_id) {
        this.nom = nom;
        this.clients = clients;
        this.gerant_id = gerant_id;
    }

    public ConseillerDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<ClientVM> getClients() {
        return clients;
    }

    public void setClients(List<ClientVM> clients) {
        this.clients = clients;
    }

    public Integer getGerant_id() {
        return gerant_id;
    }

    public void setGerant_id(Integer gerant_id) {
        this.gerant_id = gerant_id;
    }
}
