package fr.poei.open.proxybanque.controllers;

import fr.poei.open.proxybanque.dtos.UtilisateurVM;
import fr.poei.open.proxybanque.security.JwtTokens;
import fr.poei.open.proxybanque.security.RefreshRequest;
import fr.poei.open.proxybanque.services.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("auth")
public class UtilisateurController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@RequestBody UtilisateurVM utilisateurVM) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                utilisateurVM.username, utilisateurVM.password);
        Authentication authentification = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (authentification != null && authentification.isAuthenticated()) {
            JwtTokens tokens = jwtTokenService.createTokens(authentification);
            return ResponseEntity.ok().body(tokens);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<Object> refreshToken(@RequestBody RefreshRequest refreshRequest) {
        try {
            JwtTokens tokens = jwtTokenService.refreshJwtToken(refreshRequest.refreshToken);
            return ResponseEntity.ok().body(tokens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }
    }
}
