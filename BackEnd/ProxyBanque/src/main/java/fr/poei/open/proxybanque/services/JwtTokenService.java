package fr.poei.open.proxybanque.services;

import fr.poei.open.proxybanque.dtos.UtilisateurDto;
import fr.poei.open.proxybanque.security.JwtTokens;
import fr.poei.open.proxybanque.security.UserSecDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class JwtTokenService {
    private static final String USER_SECRET = "tokenSecret";

    @Value("${proxybank.security.token.secret}")
    private String secret;

    @Autowired
    private UserDetailsService userService;

    public JwtTokens createTokens(Authentication authentication) {

        String token;
        String refreshToken;

        UserSecDto user = (UserSecDto) authentication.getPrincipal();

        token = createToken(user);
        refreshToken = createRefreshToken(user);

        return new JwtTokens(token, refreshToken);
    }

    public String createToken(UserSecDto user) {

        return Jwts.builder().signWith(SignatureAlgorithm.HS512, secret).setClaims(buildUserClaims(user))
                .setExpiration(getTokenExpirationDate(false)).setIssuedAt(new Date()).compact();
    }

    public String createRefreshToken(UserSecDto user) {

        return Jwts.builder().signWith(SignatureAlgorithm.HS512, secret).setClaims(buildUserClaims(user))
                .setExpiration(getTokenExpirationDate(true)).setIssuedAt(new Date()).compact();
    }


    public Jws<Claims> validateJwtToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
    }


    public JwtTokens refreshJwtToken(String refreshToken) {
        Jws<Claims> claims = validateJwtRefreshToken(refreshToken);
        String newToken = createTokenFromClaims(claims);
        return new JwtTokens(newToken, refreshToken);
    }

    private String createTokenFromClaims(Jws<Claims> jws) {

        return Jwts.builder().setClaims(jws.getBody()).signWith(SignatureAlgorithm.HS512, secret)
                .setExpiration(getTokenExpirationDate(false)).setIssuedAt(new Date()).compact();
    }

    private Jws<Claims> validateJwtRefreshToken(String token) {
        JwtParser parser = Jwts.parser().setSigningKey(secret);
        Jws<Claims> claims = parser.parseClaimsJws(token);

        UtilisateurDto user = (UtilisateurDto) userService.loadUserByUsername(claims.getBody().getSubject());

        return parser.require(USER_SECRET, user.getTokenSecret()).parseClaimsJws(token);
    }

    private Date getTokenExpirationDate(boolean refreshToken) {
        Calendar calendar = Calendar.getInstance();

        if (refreshToken) {
            calendar.add(Calendar.MONTH, 1);
        } else {
            calendar.add(Calendar.HOUR, 12);
        }

        return calendar.getTime();
    }

    private Claims buildUserClaims(UserSecDto user) {
        Claims claims = new DefaultClaims();

        claims.setSubject(String.valueOf(user.getId()));
        claims.put("username", user.getUsername());
        claims.put("roles", String.join(",", AuthorityUtils.authorityListToSet(user.getAuthorities())));
        claims.put(USER_SECRET, user.getTokenSecret());

        return claims;
    }
}
