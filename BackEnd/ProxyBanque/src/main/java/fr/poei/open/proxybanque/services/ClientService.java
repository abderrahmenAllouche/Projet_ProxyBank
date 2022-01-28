package fr.poei.open.proxybanque.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.poei.open.proxybanque.entities.CarteBancaire;
import fr.poei.open.proxybanque.entities.CarteElectron;
import fr.poei.open.proxybanque.entities.CarteVisa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.poei.open.proxybanque.dtos.ClientDto;
import fr.poei.open.proxybanque.entities.Client;
import fr.poei.open.proxybanque.entities.CompteCourant;
import fr.poei.open.proxybanque.repositories.CarteBancaireRepository;
import fr.poei.open.proxybanque.repositories.ClientRepository;
import fr.poei.open.proxybanque.repositories.CompteCourantRepository;
import fr.poei.open.proxybanque.repositories.CompteEpargneRepository;

@Service
public class ClientService {

	@Autowired
	ClientRepository clientRepository;

	@Autowired
	CarteBancaireRepository carteBancaireRepository;

	@Autowired
	CompteCourantService compteCourantService;

	@Autowired
	CarteBancaireService carteBancaireService;

	@Autowired
	CompteService compteService;

	@Autowired
	ConseillerService conseillerService;

	@Autowired
	CompteCourantRepository compteCourantRepository;

	@Autowired
	CompteEpargneRepository compteEpargneRepository;

	public Optional<List<ClientDto>> findAllClient() {

		List<ClientDto> listeClientDto = new ArrayList<>();
		Optional<List<Client>> optionalClient = Optional.of(clientRepository.findAll());
		if (optionalClient.isPresent()) {
			if (optionalClient.get().isEmpty()) {
				return Optional.of(listeClientDto);
			} else {
				for (Client client : optionalClient.get()) {

					listeClientDto.add(findClientById(client.getId()).get());

				}
			}

		}

		return Optional.of(listeClientDto);

	}

	public boolean verficationSiIdExiste(Long id) {

		return clientRepository.existsById(id);
	}

	public boolean CreerClientSansSolde(Optional<ClientDto> optionalClientDto) {
		optionalClientDto.get().getIdConseiller();

		CompteCourant compteCourant = new CompteCourant();
		CarteElectron carteElectron = new CarteElectron();

		Optional<Client> optionalClient = Optional
				.of(new Client(optionalClientDto.get().getNom(), optionalClientDto.get().getPreNom(),
						optionalClientDto.get().getAdresse(), optionalClientDto.get().getTel(), compteCourant));

		if (!optionalClient.isEmpty() && optionalClient.isPresent()) {

			optionalClient = Optional.of(clientRepository.save(optionalClient.get()));
			optionalClient.get()
					.setCompteCourant(compteCourantService.creerCompteCourant(compteCourant, optionalClient));
			optionalClient.get().setCarteElectron(carteElectron);
			optionalClient = Optional.of(clientRepository.save(optionalClient.get()));
			if (clientRepository.findById(optionalClient.get().getId()).isPresent()) {
				return true;
			} else {
				return false;
			}
		}

		else {
			return false;
		}

	}

	public boolean CreerClientAvecSolde(Optional<ClientDto> optionalClientDto) {


		CompteCourant compteCourant = new CompteCourant();
		CarteElectron carteElectron = new CarteElectron();
		CarteVisa carteVisa = new CarteVisa();
		compteCourant.setSolde(optionalClientDto.get().getCompteCourant().getSolde());

		Optional<Client> optionalClient = Optional
				.of(new Client(optionalClientDto.get().getNom(), optionalClientDto.get().getPreNom(),
						optionalClientDto.get().getAdresse(), optionalClientDto.get().getTel(), compteCourant));

		if (!optionalClient.isEmpty() && optionalClient.isPresent()) {

			optionalClient = Optional.of(clientRepository.save(optionalClient.get()));
			optionalClient.get()
					.setCompteCourant(compteCourantService.creerCompteCourant(compteCourant, optionalClient));

			if (optionalClient.get().getCompteCourant().getSolde() < 300) {
				optionalClient.get().setCarteElectron(
						carteBancaireService.creerCBElectron(Optional.of(carteElectron), optionalClient));
			} else {

				optionalClient.get()
						.setCarteVisa(carteBancaireService.creerCBVisa(Optional.of(carteVisa), optionalClient));
			}
			optionalClient = Optional.of(clientRepository.save(optionalClient.get()));

			if (clientRepository.findById(optionalClient.get().getId()).isPresent()) {
				this.conseillerService.assigneClientConseiller(optionalClient.get().getId(), optionalClientDto.get().getIdConseiller());
				return true;
			} else {
				return false;
			}
		}

		else {
			return false;
		}

	}

	public Optional<ClientDto> findClientById(Long idClient) {
		Optional<Client> optionalClient = clientRepository.findById(idClient);
		if (optionalClient.isPresent()) {
			ClientDto clientDto = new ClientDto();
			clientDto.setId(optionalClient.get().getId());
			clientDto.setNom(optionalClient.get().getNom());
			clientDto.setPreNom(optionalClient.get().getPreNom());
			clientDto.setAdresse(optionalClient.get().getAdresse());
			clientDto.setTel(optionalClient.get().getTel());

			Optional<CarteBancaire> typeCarte = carteBancaireService.findByIdCB(optionalClient.get().getId());

			if (("carte_Visa").equals(typeCarte.get().getTypeCarte())) {
				CarteVisa carteVisa = (CarteVisa) typeCarte.get();
				clientDto.setCarteVisa(carteVisa);
				clientDto.getCarteVisa().setId(carteVisa.getId());
				clientDto.getCarteVisa().setNumCarte(carteVisa.getNumCarte());

			} else {
				CarteElectron carteElectron = (CarteElectron) typeCarte.get();
				clientDto.setCarteElectron(carteElectron);
				clientDto.getCarteElectron().setId(carteElectron.getId());
				clientDto.getCarteElectron().setNumCarte(carteElectron.getNumCarte());
			}

			if (optionalClient.get().getConseiller() != null) {
				clientDto.setIdConseiller(optionalClient.get().getConseiller().getId());
			}

			clientDto.setCompteCourant(optionalClient.get().getCompteCourant());

			if (optionalClient.get().getCompteEpargne() != null) {
				clientDto.setCompteEpargne(optionalClient.get().getCompteEpargne());
			}

			return Optional.of(clientDto);

		} else {
			return Optional.empty();
		}
	}

	public boolean updateClientById(Optional<ClientDto> clientDto) {
		Optional<Client> updateClient = this.clientRepository.findById(clientDto.get().getId());

		if (updateClient.isPresent()&&!updateClient.isEmpty()) {
			if (clientDto.get().getAdresse()!=null){
				updateClient.get().setAdresse(clientDto.get().getAdresse());
			}
			if (clientDto.get().getNom()!=null){
				updateClient.get().setNom(clientDto.get().getNom());
			}
			if (clientDto.get().getTel()!=null){
				updateClient.get().setTel(clientDto.get().getTel());
			}
			if (clientDto.get().getPreNom()!=null){
				updateClient.get().setPreNom(clientDto.get().getPreNom());
			}
			clientRepository.save(updateClient.get());
			return true;
		} else {
			return false;
		}

	}

	public boolean deleteClient(Long idClient) {
		Boolean resultat = clientRepository.existsById(idClient);

		if (resultat) {
			// Supprimer le compte courant et la carte  en même temps
			compteCourantRepository.delete(clientRepository.getById(idClient).getCompteCourant());

			carteBancaireRepository.delete(carteBancaireRepository.getById(clientRepository.getById(idClient).getIdCarteBancaire()));

			return true;

		} else {

			return false;
		}

	}

	public boolean virement(Long idCompteDebiteur, Long idCompteCrediteur, Long montantVirement) {

		boolean soustractionMontant = compteService.soustractionSoldeByTypeCompte(idCompteDebiteur, montantVirement);



		boolean ajoutMontant = compteService.AjoutSoldeByTypeCompte(idCompteCrediteur, montantVirement);



		return (soustractionMontant && ajoutMontant);

	}

}
