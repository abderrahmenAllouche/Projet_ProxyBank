package fr.poei.open.proxybanque.dtos;

public class ConseillerVM {
    private Long id;
    private String nom;
    private Integer gerant_id;

    public ConseillerVM() {
        super();
    }

    public ConseillerVM(Long id, String nom, Integer gerant_id) {
        super();
        this.id = id;
        this.nom = nom;
        this.gerant_id = gerant_id;
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

    public Integer getGerant_id() {
        return gerant_id;
    }

    public void setGerant_id(Integer gerant_id) {
        this.gerant_id = gerant_id;
    }


}
