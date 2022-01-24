package fr.poei.open.proxybanque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.poei.open.proxybanque.entities.CompteCourant;

@Repository
public interface CompteCourantRepository extends JpaRepository<CompteCourant, Long>{


}
