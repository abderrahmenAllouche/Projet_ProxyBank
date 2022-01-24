package fr.poei.open.proxybanque.repositories;

import fr.poei.open.proxybanque.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository  extends JpaRepository<Role, Integer> {

    Optional<Role> findByNom(String nom);
}
