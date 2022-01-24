package fr.poei.open.proxybanque.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AdminUserDefaultConf {
    @Value("${proxybank.admin.username}")
    private String username;

    @Value("${proxybank.admin.password}")
    private String password;

    public AdminUserDefaultConf(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AdminUserDefaultConf() {
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
}
