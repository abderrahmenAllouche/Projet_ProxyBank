package fr.poei.open.ProxyBanque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.poei.open.ProxyBanque.entities.Client;



@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{

}
