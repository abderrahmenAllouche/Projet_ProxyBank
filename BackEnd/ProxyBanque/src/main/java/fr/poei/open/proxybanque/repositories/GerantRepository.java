package fr.poei.open.proxybanque.repositories;

import fr.poei.open.proxybanque.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import fr.poei.open.proxybanque.entities.Gerant;

import java.util.Optional;

@Repository
public interface GerantRepository extends JpaRepository<Gerant, Integer> {
    Optional<Gerant> findGerantByUtilisateur(Utilisateur utilisateur);
}
