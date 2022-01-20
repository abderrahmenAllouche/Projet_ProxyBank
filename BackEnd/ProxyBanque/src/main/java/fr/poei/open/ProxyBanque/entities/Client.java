package fr.poei.open.ProxyBanque.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Conseiller conseiller;

    //Cascade sert à corriger l'exception :TransientObjectException
    @OneToOne(cascade = CascadeType.ALL)
    private CompteCourant compteCourant;

    @OneToOne(cascade = CascadeType.ALL)
    private CompteEpargne compteEpargne;

    //	@OneToOne(cascade = CascadeType.ALL)
    private Long idCarteBancaire ;

    @OneToOne(cascade = CascadeType.ALL)
    private CarteElectron carteElectron;

    @OneToOne(cascade = CascadeType.ALL)
    private CarteVisa carteVisa;


    //private CarteBancaire carteBancaire;
    private String nom;
    private String preNom;
    private String adresse;
    private Integer tel;


    public Client() {
        super();
    }

    public Client(String nom, String preNom, String adresse, Integer tel,  CompteCourant compteCourant) {
        super();


        this.compteCourant = compteCourant;
        this.nom = nom;
        this.preNom = preNom;
        this.adresse = adresse;
        this.tel = tel;
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

    public Conseiller getConseiller() {
        return conseiller;
    }

    public void setConseiller(Conseiller conseiller) {
        this.conseiller = conseiller;
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

    public Long getIdCarteBancaire() {
        return idCarteBancaire;
    }

    public void setIdCarteBancaire(Long idCarteBancaire) {
        this.idCarteBancaire = idCarteBancaire;
    }

    public void setCarteVisa(CarteVisa carteVisa) {

        this.carteVisa = carteVisa ;
    }

    public void setCarteElectron(CarteElectron carteElectron) {

        this.carteElectron = carteElectron ;
    }

    @Override
    public String toString() {
        return "Client [id=" + id + ", conseiller=" + conseiller + ", compteCourant=" + compteCourant
                + ", compteEpargne=" + compteEpargne + ", nom=" + nom + ", preNom=" + preNom + ", adresse=" + adresse
                + ", tel=" + tel + "]";
    }





}
