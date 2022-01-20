package fr.poei.open.ProxyBanque.repositories;

import fr.poei.open.ProxyBanque.entities.Conseiller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConseillerRepository extends JpaRepository<Conseiller, Long> {
}
