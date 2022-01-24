package fr.poei.open.proxybanque.controllers;

import fr.poei.open.proxybanque.dtos.UserRoleVM;
import fr.poei.open.proxybanque.services.JwtTokenService;
import fr.poei.open.proxybanque.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping("/user")
    public ResponseEntity<Object> createUser(@RequestBody UserRoleVM userRoleVM) {
        System.err.println(userRoleVM);
        userRoleVM.setPassword(this.encoder.encode(userRoleVM.getPassword()));
        return ResponseEntity.status(HttpStatus.OK).body(this.userDetailsServiceImpl.createAccount(userRoleVM));
    }
}
