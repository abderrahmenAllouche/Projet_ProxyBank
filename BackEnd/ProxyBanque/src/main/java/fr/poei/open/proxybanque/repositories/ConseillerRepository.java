package fr.poei.open.proxybanque.repositories;

import fr.poei.open.proxybanque.entities.Conseiller;
import fr.poei.open.proxybanque.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConseillerRepository extends JpaRepository<Conseiller, Long> {
    Optional<Conseiller> findConseillerByUtilisateur(Utilisateur utilisateur);

}
