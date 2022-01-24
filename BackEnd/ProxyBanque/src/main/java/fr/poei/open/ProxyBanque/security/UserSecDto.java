package fr.poei.open.proxybanque.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.poei.open.proxybanque.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.util.Collection;

public class UserSecDto extends User {

    private static final long serialVersionUID = 1L;

    private final int id;
    private final LocalDate lastPasswordResetDate;
    private final String tokenSecret;

    @Autowired
    UtilisateurRepository userRepository;

    public UserSecDto(int id, String username, String password, Collection<? extends GrantedAuthority> authorities, boolean enabled, LocalDate lastPasswordResetDate, String userSecret) {
        super(username, password, enabled, true, true, true, authorities);
        this.id = id;
        this.lastPasswordResetDate = lastPasswordResetDate;
        this.tokenSecret = userSecret;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    public LocalDate getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return super.isAccountNonExpired();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return super.isAccountNonLocked();
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return super.isCredentialsNonExpired();
    }

    @JsonIgnore
    public String getTokenSecret() {
        return tokenSecret;
    }
}
