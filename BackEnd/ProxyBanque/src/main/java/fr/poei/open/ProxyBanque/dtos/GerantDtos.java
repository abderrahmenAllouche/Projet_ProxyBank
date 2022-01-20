package fr.poei.open.ProxyBanque.dtos;

import java.util.ArrayList;
import java.util.List;

import fr.poei.open.ProxyBanque.entities.Conseiller;

public class GerantDtos {
    private Integer id;
    private String nom;
    private List<ConseillerVM> conseillers = new ArrayList<>();
    private Integer idAgence;

    public GerantDtos(Integer id, String nom, List<ConseillerVM> conseillers, Integer idAgence) {
        super();
        this.id = id;
        this.nom = nom;
        this.conseillers = conseillers;
        this.idAgence = idAgence;
    }

    public GerantDtos(Integer id, String nom, List<ConseillerVM> conseillers) {
        super();
        this.id = id;
        this.nom = nom;
        this.conseillers = conseillers;
    }

    public GerantDtos() {
        super();
    }

    public Integer getIdAgence() {
        return idAgence;
    }

    public void setIdAgence(Integer idAgence) {
        this.idAgence = idAgence;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<ConseillerVM> getConseillers() {
        return conseillers;
    }

    public void setConseillers(List<ConseillerVM> conseillers) {
        this.conseillers = conseillers;
    }

    @Override
    public String toString() {
        return "GerantDtos [id=" + id + ", nom=" + nom + ", conseillers=" + conseillers + "]";
    }

}
