package fr.poei.open.proxybanque.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.poei.open.proxybanque.dtos.CarteBancaireDto;
import fr.poei.open.proxybanque.dtos.ClientDto;
import fr.poei.open.proxybanque.entities.CarteBancaire;
import fr.poei.open.proxybanque.entities.CarteElectron;
import fr.poei.open.proxybanque.entities.CarteVisa;
import fr.poei.open.proxybanque.entities.Client;
import fr.poei.open.proxybanque.repositories.CarteBancaireRepository;
import fr.poei.open.proxybanque.repositories.ClientRepository;

@Service
public class CarteBancaireService {

	@Autowired
	CarteBancaireRepository carteBancaireRepository;

	@Autowired
	ClientRepository clientRepository;

	public CarteVisa creerCBVisa(Optional<CarteBancaire> carte, Optional<Client> optionalClient) {

		CarteVisa carteVisa = new CarteVisa(carte.get().getTypeCarte(), optionalClient.get());

		String numCarte = Long.toString(optionalClient.get().getId()) + Integer.toString(LocalDate.now().getYear())
				+ Integer.toString(LocalDate.now().getMonthValue()) + Integer.toString(LocalDate.now().getDayOfMonth());
		carteVisa.setNumCarte(numCarte);

		carteVisa = carteBancaireRepository.save(carteVisa);

		optionalClient.get().setCarteVisa(carteVisa);
		optionalClient.get().setIdCarteBancaire(carteVisa.getId());
		optionalClient = Optional.of(clientRepository.save(optionalClient.get()));

		return carteVisa;
	}

	public CarteElectron creerCBElectron(Optional<CarteBancaire> carte, Optional<Client> optionalClient) {

		CarteElectron carteElectron = new CarteElectron(carte.get().getTypeCarte(), optionalClient.get());

		String numCarte = Long.toString(optionalClient.get().getId()) + Integer.toString(LocalDate.now().getYear())
				+ Integer.toString(LocalDate.now().getMonthValue()) + Integer.toString(LocalDate.now().getDayOfMonth());
		carteElectron.setNumCarte(numCarte);

		carteElectron = carteBancaireRepository.save(carteElectron);

		optionalClient.get().setCarteElectron(carteElectron);
		optionalClient.get().setIdCarteBancaire(carteElectron.getId());
		optionalClient = Optional.of(clientRepository.save(optionalClient.get()));

		return carteElectron;
	}

	public List<CarteBancaire> findAllCB() {

		return carteBancaireRepository.findAll();

	}

	public Optional<CarteBancaire> findByIdCB(Long idClient) {
		Optional<List<CarteBancaire>> optionalListCarteBancaire = Optional.of(carteBancaireRepository.findAll());

		if (optionalListCarteBancaire.isPresent() && !optionalListCarteBancaire.isEmpty()) {
			for (CarteBancaire carteBancaire : optionalListCarteBancaire.get()) {
				if (carteBancaire.getClient().getId() == idClient) {

					return Optional.of(carteBancaire);

				}
			}

		} else {
			return Optional.empty();
		}
		return Optional.empty();

	}

	public boolean deleteCB(Long idClient) {

		Optional<Client> optionalClient = clientRepository.findById(idClient);

		Optional<CarteBancaire> optionalCarteBancaire = carteBancaireRepository
				.findById(optionalClient.get().getIdCarteBancaire());
		Long idCarte = optionalClient.get().getIdCarteBancaire();

		if (optionalCarteBancaire.get().getTypeCarte().equals("carte_Visa")) {

			optionalClient.get().setIdCarteBancaire(null);
			optionalClient.get().setCarteVisa(null);

			clientRepository.save(optionalClient.get());

			carteBancaireRepository.deleteById(idCarte);
			return true;

		} else {

			optionalClient.get().setCarteElectron(null);
			return true;
		}

	}
}
