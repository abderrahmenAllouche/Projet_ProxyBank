package fr.poei.open.ProxyBanque.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.poei.open.ProxyBanque.entities.CarteBancaire;
import fr.poei.open.ProxyBanque.services.CarteBancaireService;
import fr.poei.open.ProxyBanque.services.ClientService;

@RestController
@RequestMapping("/ProxyBank")
public class CarteBancaireController {


    @Autowired
    CarteBancaireService carteBancaireService;

    @Autowired
    ClientService clientService;

    @GetMapping("/CarteBancaire")
    public List<CarteBancaire> findAllCB() {
        return carteBancaireService.findAllCB();
    }

    @GetMapping("/CarteBancaire/{id}")
    public Optional<CarteBancaire> findByIdCB(@PathVariable Long id) {
        return carteBancaireService.findByIdCB(id);
    }

    @DeleteMapping("/CarteBancaire/{idClient}")
    public void deleteCB(@PathVariable("idClient") String idClient) {

        carteBancaireService.deleteCB(Long.parseLong(idClient));
    }

}
