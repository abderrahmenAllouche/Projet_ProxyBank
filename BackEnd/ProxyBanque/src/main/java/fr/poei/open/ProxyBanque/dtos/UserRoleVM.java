package fr.poei.open.proxybanque.dtos;

public class UserRoleVM {
    private String username;
    private String password;
    private String role;
    private String nom;

    public UserRoleVM() {
    }

    @Override
    public String toString() {
        return "UserRoleVM{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", nom='" + nom + '\'' +
                '}';
    }

    public UserRoleVM(String username, String password, String role, String nom) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.nom = nom;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
