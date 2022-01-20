package fr.poei.open.ProxyBanque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.poei.open.ProxyBanque.entities.CompteEpargne;

@Repository
public interface CompteEpargneRepository extends JpaRepository<CompteEpargne, Long> {

}
