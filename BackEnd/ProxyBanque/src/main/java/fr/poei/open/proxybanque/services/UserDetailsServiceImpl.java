package fr.poei.open.proxybanque.services;

import fr.poei.open.proxybanque.dtos.UserRoleVM;
import fr.poei.open.proxybanque.entities.Conseiller;
import fr.poei.open.proxybanque.entities.Gerant;
import fr.poei.open.proxybanque.entities.Role;
import fr.poei.open.proxybanque.entities.Utilisateur;
import fr.poei.open.proxybanque.repositories.ConseillerRepository;
import fr.poei.open.proxybanque.repositories.GerantRepository;
import fr.poei.open.proxybanque.repositories.RoleRepository;
import fr.poei.open.proxybanque.repositories.UtilisateurRepository;
import fr.poei.open.proxybanque.security.UserSecDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UtilisateurRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ConseillerRepository conseillerRepository;

    @Autowired
    private GerantRepository gerantRepository;

    @Autowired
    private GerantService gerantService;

    @Autowired
    private ConseillerService conseillerService;

    @Override
    public UserSecDto loadUserByUsername(String username) {

        Objects.requireNonNull(username);
        Utilisateur userE = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userE.getRole().getNom()));

        return new UserSecDto(
                userE.getId(),
                userE.getUsername(),
                userE.getPassword(),
                authorities,
                userE.getActif(),
                LocalDate.now(),
                userE.getTokenSecret());
    }

    public UserSecDto createAccount(UserRoleVM userRoleVM) {
        Optional<Role> optionalRole = this.roleRepository.findByNom(userRoleVM.getRole());
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setActif(true);
        utilisateur.setRole(optionalRole.get());
        utilisateur.setUsername(userRoleVM.getUsername());
        utilisateur.setPassword(userRoleVM.getPassword());
        utilisateur = this.userRepository.save(utilisateur);

        switch (utilisateur.getRole().getNom()) {
            case "CONSEILLER":
                Conseiller conseiller = new Conseiller();
                conseiller.setNom(userRoleVM.getNom());
                conseiller.setUtilisateur(utilisateur);

                Optional<Gerant> gerant2 = this.gerantRepository.findById(Integer.parseInt(userRoleVM.getSuperieurId()));
                conseiller.setGerant(gerant2.get());
                conseiller = this.conseillerRepository.save(conseiller);
                this.gerantService.assigneConseillerGerant(conseiller.getId(), Integer.parseInt(userRoleVM.getSuperieurId()));

                break;
            case "GERANT":
                Gerant gerant = new Gerant();
                gerant.setNom(userRoleVM.getNom());
                gerant.setUtilisateur(utilisateur);
                gerant = this.gerantRepository.save(gerant);
                this.gerantService.assigneAgenceGerant(Integer.parseInt(userRoleVM.getSuperieurId()), gerant.getId());
                break;

            default: break;
        }

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(utilisateur.getRole().getNom()));
        return new UserSecDto(
                utilisateur.getId(),
                utilisateur.getUsername(),
                utilisateur.getPassword(),
                authorities,
                utilisateur.getActif(),
                LocalDate.now(),
                utilisateur.getTokenSecret());
    }

    public Optional<List<Utilisateur>> findAllConseiller() {
        List<Utilisateur> utilisateurs = this.userRepository.findAll();
        return Optional.of(utilisateurs);
    }

    public Boolean verifUserName(String username) {
        Optional<Utilisateur> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            if (!user.isEmpty()){
                return false;
            }
        }
        return true;

    }
}