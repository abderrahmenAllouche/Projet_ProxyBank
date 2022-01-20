package fr.poei.open.ProxyBanque.dtos;

import fr.poei.open.ProxyBanque.entities.CarteElectron;
import fr.poei.open.ProxyBanque.entities.CarteVisa;
import fr.poei.open.ProxyBanque.entities.CompteCourant;
import fr.poei.open.ProxyBanque.entities.CompteEpargne;
import fr.poei.open.ProxyBanque.entities.Conseiller;

public class ClientDto {
    private Long id;
    private String nom;
    private String preNom;
    private String adresse;
    private Integer tel;

    private CarteElectron carteElectron;
    private CarteVisa carteVisa;

    private Long idConseiller;
    private CompteCourant compteCourant;
    private CompteEpargne compteEpargne;


    public ClientDto(Long id, String nom, String preNom, String adresse, Integer tel, Conseiller conseiller, CompteCourant compteCourant, CompteEpargne compteEpargne) {
        super();
        this.id = id;
        this.nom = nom;
        this.preNom = preNom;
        this.adresse = adresse;
        this.tel = tel;
        this.idConseiller = idConseiller;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClientDto() {
        super();
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPreNom() {
        return preNom;
    }

    public void setPreNom(String preNom) {
        this.preNom = preNom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Integer getTel() {
        return tel;
    }

    public void setTel(Integer tel) {
        this.tel = tel;
    }

    public CompteCourant getCompteCourant() {
        return compteCourant;
    }

    public void setCompteCourant(CompteCourant compteCourant) {
        this.compteCourant = compteCourant;
    }

    public CompteEpargne getCompteEpargne() {
        return compteEpargne;
    }

    public void setCompteEpargne(CompteEpargne compteEpargne) {
        this.compteEpargne = compteEpargne;
    }

    public Long getIdConseiller() {
        return idConseiller;
    }

    public void setIdConseiller(Long idConseiller) {
        this.idConseiller = idConseiller;
    }

    public CarteElectron getCarteElectron() {
        return carteElectron;
    }

    public void setCarteElectron(CarteElectron carteElectron) {
        this.carteElectron = carteElectron;
    }

    public CarteVisa getCarteVisa() {
        return carteVisa;
    }

    public void setCarteVisa(CarteVisa carteVisa) {
        this.carteVisa = carteVisa;
    }

    @Override
    public String toString() {
        return "ClientDto [id=" + id + ", nom=" + nom + ", preNom=" + preNom + ", adresse=" + adresse + ", tel=" + tel
                + "]";
    }

}
