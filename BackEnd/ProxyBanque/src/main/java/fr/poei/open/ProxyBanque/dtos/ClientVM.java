package fr.poei.open.proxybanque.dtos;

public class ClientVM {
    private Long id;
    private String nom;
    private Long conseiller_id;

    public ClientVM() {
    }

    public ClientVM(Long id, String nom, Long conseiller_id) {
        this.id = id;
        this.nom = nom;
        this.conseiller_id = conseiller_id;
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
}
