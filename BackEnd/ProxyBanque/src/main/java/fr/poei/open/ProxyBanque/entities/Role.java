package fr.poei.open.proxybanque.entities;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@DynamicInsert
@DynamicUpdate
public class Role {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_SEQ")
    private Integer Id;

    @Column(unique=true)
    private String nom;

    @Override
    public String toString() {
        return "Role{" +
                "Id=" + Id +
                ", nom='" + nom + '\'' +
                '}';
    }

    public Role(Integer id, String nom) {
        Id = id;
        this.nom = nom;
    }

    public Role() {
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
