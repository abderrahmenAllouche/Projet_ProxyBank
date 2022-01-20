package fr.poei.open.ProxyBanque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import fr.poei.open.ProxyBanque.entities.Gerant;

@Repository
public interface GerantRepository extends JpaRepository<Gerant, Integer> {

}
