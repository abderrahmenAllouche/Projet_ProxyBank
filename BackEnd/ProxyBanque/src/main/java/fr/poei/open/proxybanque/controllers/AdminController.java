package fr.poei.open.proxybanque.controllers;

import fr.poei.open.proxybanque.dtos.ResponseBodyDto;
import fr.poei.open.proxybanque.dtos.UserRoleVM;
import fr.poei.open.proxybanque.entities.Utilisateur;
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

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("admin")
@PreAuthorize("hasAnyAuthority('GERANT','ADMIN')")
public class AdminController {
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping("/user")
    public ResponseEntity<Object> createUser(@RequestBody UserRoleVM userRoleVM) {
        Boolean username = this.userDetailsServiceImpl.verifUserName(userRoleVM.getUsername());
        if(username){
            userRoleVM.setPassword(this.encoder.encode(userRoleVM.getPassword()));
            return ResponseEntity.status(HttpStatus.OK).body(this.userDetailsServiceImpl.createAccount(userRoleVM));
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'UserName est déjà utilisé !"));
        }
    }

    @PreAuthorize("hasAnyAuthority('GERANT','ADMIN')")
    @GetMapping("/user")
    @ResponseBody
    public ResponseEntity<?> findAllConseiller(){
        Optional<List<Utilisateur>> optionalConseillerDto = userDetailsServiceImpl.findAllConseiller();
        if (optionalConseillerDto.isPresent()){
            if (optionalConseillerDto.get().isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("La liste est vide"));
            }else{
                return ResponseEntity.status(HttpStatus.OK).body(optionalConseillerDto.get());
            }
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("La liste est vide"));
        }
    }
}