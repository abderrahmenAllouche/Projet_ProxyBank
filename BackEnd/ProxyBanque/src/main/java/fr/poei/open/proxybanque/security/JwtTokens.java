package fr.poei.open.proxybanque.security;

public class JwtTokens {
    public String token;
    public String refreshToken;

    public JwtTokens(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
