package fr.poei.open.ProxyBanque.dtos;

public class ClientSoldeVM {
    private Long id;
    private String nom;
    private Long conseiller_id;
    private Long solde;

    public ClientSoldeVM(Long id, String nom, Long conseiller_id, Long solde) {
        super();
        this.id = id;
        this.nom = nom;
        this.conseiller_id = conseiller_id;
        this.solde = solde;
    }

    public ClientSoldeVM() {
        super();
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

    public Long getConseiller_id() {
        return conseiller_id;
    }

    public void setConseiller_id(Long conseiller_id) {
        this.conseiller_id = conseiller_id;
    }

    public Long  getSolde() {
        return solde;
    }

    public void setSolde(Long solde) {
        this.solde = solde;
    }

}
