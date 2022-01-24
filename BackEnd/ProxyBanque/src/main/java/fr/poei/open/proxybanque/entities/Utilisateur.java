package fr.poei.open.proxybanque.entities;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@DynamicInsert
@DynamicUpdate
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
    private Integer id;
    private String tokenSecret;
    private String password;

    @Column(unique = true)
    private String username;

    @ManyToOne
    @JoinColumn(name = "role")
    private Role role;
    private Boolean actif;

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", tokenSecret='" + tokenSecret + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", actif=" + actif +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Utilisateur() {
    }

    public Utilisateur(Integer id, String tokenSecret, String password, String username, Role role, Boolean actif) {
        this.id = id;
        this.tokenSecret = tokenSecret;
        this.password = password;
        this.username = username;
        this.role = role;
        this.actif = actif;
    }
}
