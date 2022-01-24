package fr.poei.open.proxybanque.repositories;

import fr.poei.open.proxybanque.entities.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {

    public List<Utilisateur> findAll();

    public Page<Utilisateur> findAll(Pageable firstPageWithTwoElements);

    Optional<Utilisateur> findUserByUsername(String username);

    @Query(value = "SELECT * FROM Utilisateur u WHERE u.role = :id",
            nativeQuery = true)
    public List<Utilisateur> UtilisateursByRole(int id);

    public Optional<Utilisateur> findByUsername(String cap);

    @Query(value = "SELECT * FROM Utilisateur u WHERE u.reservation = true",
            nativeQuery = true)
    public List<Utilisateur> UtilisateursAvecReservationActive();


    @Query(value = "SELECT * FROM Utilisateur u WHERE u.actif = :active",
            nativeQuery = true)
    public List<Utilisateur> UtilisateursActive(int active);


}
