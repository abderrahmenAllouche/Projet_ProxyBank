package fr.poei.open.proxybanque.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UtilisateurDto {

    private Integer id;
    private String username;
    private String password;
    private String tokenSecret;
    private Boolean actif;

    @Override
    public String toString() {
        return "UtilisateurDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", tokenSecret='" + tokenSecret + '\'' +
                ", actif=" + actif +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public UtilisateurDto() {
    }

    public UtilisateurDto(Integer id, String username, String password, String tokenSecret, Boolean actif) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.tokenSecret = tokenSecret;
        this.actif = actif;
    }
}
