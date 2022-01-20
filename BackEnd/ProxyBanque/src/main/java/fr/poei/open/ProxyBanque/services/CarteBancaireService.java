package fr.poei.open.ProxyBanque.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.poei.open.ProxyBanque.dtos.CarteBancaireDto;
import fr.poei.open.ProxyBanque.dtos.ClientDto;
import fr.poei.open.ProxyBanque.dtos.CompteEpargneDto;
import fr.poei.open.ProxyBanque.entities.CarteBancaire;
import fr.poei.open.ProxyBanque.entities.CarteElectron;
import fr.poei.open.ProxyBanque.entities.CarteVisa;
import fr.poei.open.ProxyBanque.entities.Client;
import fr.poei.open.ProxyBanque.entities.CompteEpargne;
import fr.poei.open.ProxyBanque.repositories.CarteBancaireRepository;
import fr.poei.open.ProxyBanque.repositories.ClientRepository;

@Service
public class CarteBancaireService {

    @Autowired
    CarteBancaireRepository carteBancaireRepository;

    @Autowired
    ClientRepository clientRepository;

    public boolean creerCBVisa(Optional<CarteBancaireDto> carte, Optional<ClientDto> optionalClientDto) {

        Optional<Client> optionalClient = clientRepository.findById(optionalClientDto.get().getId());


        Optional<CarteVisa> optionalCarteVisa = Optional.of(new CarteVisa(carte.get().getTypeCarte(), optionalClient.get()));

        String numCarte = Long.toString(optionalClient.get().getId()) + Integer.toString(LocalDate.now().getYear())
                + Integer.toString(LocalDate.now().getMonthValue()) + Integer.toString(LocalDate.now().getDayOfMonth());
        optionalCarteVisa.get().setNumCarte(numCarte);



        if (optionalCarteVisa.isPresent() && !optionalCarteVisa.isEmpty()) {
            optionalCarteVisa = Optional.of(carteBancaireRepository.save(optionalCarteVisa.get()));
            optionalClient.get().setCarteVisa(optionalCarteVisa.get());
            optionalClient.get().setIdCarteBancaire(optionalCarteVisa.get().getId());
            optionalClient = Optional.of(clientRepository.save(optionalClient.get()));
            if (carteBancaireRepository.findById(optionalCarteVisa.get().getId()).isPresent()) {
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    public boolean creerCBElectron(Optional<CarteBancaireDto> carte, Optional<ClientDto> optionalClientDto) {

        Optional<Client> optionalClient = clientRepository.findById(optionalClientDto.get().getId());

        Optional<CarteElectron> optionalCarteElectron = Optional
                .of(new CarteElectron(carte.get().getTypeCarte(), optionalClient.get()));

        String numCarte = Long.toString(optionalClient.get().getId()) + Integer.toString(LocalDate.now().getYear())
                + Integer.toString(LocalDate.now().getMonthValue()) + Integer.toString(LocalDate.now().getDayOfMonth());
        optionalCarteElectron.get().setNumCarte(numCarte);

        if (optionalCarteElectron.isPresent() && !optionalCarteElectron.isEmpty()) {
            optionalCarteElectron = Optional.of(carteBancaireRepository.save(optionalCarteElectron.get()));
            optionalClient.get().setCarteElectron(optionalCarteElectron.get());
            optionalClient.get().setIdCarteBancaire(optionalCarteElectron.get().getId());
            optionalClient = Optional.of(clientRepository.save(optionalClient.get()));
            if (carteBancaireRepository.findById(optionalCarteElectron.get().getId()).isPresent()) {
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    public List<CarteBancaire> findAllCB() {

        return carteBancaireRepository.findAll();

    }

    public Optional<CarteBancaire> findByIdCB(Long idClient) {

        Optional<CarteBancaire> optionalCarteBancaire = carteBancaireRepository.findById(idClient);

        if (optionalCarteBancaire.isPresent()) {
            if (optionalCarteBancaire.get().getTypeCarte() == "Carte_Visa") {
                return Optional.of(new CarteVisa(optionalCarteBancaire.get().getTypeCarte(),
                        optionalCarteBancaire.get().getClient()));
            } else {
                return Optional.of(new CarteElectron(optionalCarteBancaire.get().getTypeCarte(),
                        optionalCarteBancaire.get().getClient()));

            }
        } else {
            return Optional.empty();
        }

    }

    public boolean deleteCB(Long idClient) {

        Optional<Client> optionalClient = clientRepository.findById(idClient);

        Optional<CarteBancaire> optionalCarteBancaire = carteBancaireRepository.findById(optionalClient.get().getIdCarteBancaire());
        Long idCarte = optionalClient.get().getIdCarteBancaire();

        if(optionalCarteBancaire.get().getTypeCarte().equals("Visa")) {

            optionalClient.get().setIdCarteBancaire(null);
            optionalClient.get().setCarteVisa(null);

            clientRepository.save(optionalClient.get());

            carteBancaireRepository.deleteById(idCarte);
            return true;

        }else{

            optionalClient.get().setCarteElectron(null);
            return true;
        }



    }
}
