package fr.poei.open.ProxyBanque.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.poei.open.ProxyBanque.entities.CarteElectron;
import fr.poei.open.ProxyBanque.entities.CarteVisa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.poei.open.ProxyBanque.dtos.ClientDto;
import fr.poei.open.ProxyBanque.entities.Client;
import fr.poei.open.ProxyBanque.entities.CompteCourant;
import fr.poei.open.ProxyBanque.repositories.ClientRepository;
import fr.poei.open.ProxyBanque.repositories.CompteCourantRepository;
import fr.poei.open.ProxyBanque.repositories.CompteEpargneRepository;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CompteCourantService compteCourantService;



    @Autowired
    CompteService compteService;

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

                    ClientDto clientDto = new ClientDto(client.getId(), client.getNom(), client.getPreNom(),
                            client.getAdresse(), client.getTel(), client.getConseiller(),
                            client.getCompteCourant(), client.getCompteEpargne());

                    clientDto.setId(client.getId());
                    listeClientDto.add(clientDto);

                }
            }

        }

        return Optional.of(listeClientDto);

    }

    public boolean verficationSiIdExiste(Long id) {

        return clientRepository.existsById(id);
    }

    public boolean CreerClientSansSolde(Optional<ClientDto> optionalClientDto) {

        CompteCourant compteCourant = new CompteCourant();
        CarteElectron carteElectron = new CarteElectron();

        Optional<Client> optionalClient = Optional.of(new Client(optionalClientDto.get().getNom(),
                optionalClientDto.get().getPreNom(), optionalClientDto.get().getAdresse(),
                optionalClientDto.get().getTel(),  compteCourant));

        if (!optionalClient.isEmpty() && optionalClient.isPresent()) {

            optionalClient = Optional.of(clientRepository.save(optionalClient.get()));
            optionalClient.get().setCompteCourant(compteCourantService.creerCompteCourant(compteCourant, optionalClient));
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

        Optional<Client> optionalClient = Optional.of(new Client(optionalClientDto.get().getNom(),
                optionalClientDto.get().getPreNom(), optionalClientDto.get().getAdresse(),
                optionalClientDto.get().getTel(),  compteCourant));

        if (!optionalClient.isEmpty() && optionalClient.isPresent()) {

            optionalClient = Optional.of(clientRepository.save(optionalClient.get()));
            optionalClient.get().setCompteCourant(compteCourantService.creerCompteCourant(compteCourant, optionalClient));

            if(optionalClient.get().getCompteCourant().getSolde()<300) {
                optionalClient.get().setCarteElectron(carteElectron);
            }else {
                optionalClient.get().setCarteVisa(carteVisa);
            }
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

    public Optional<ClientDto> findClientById(Long idClient) {
        Optional<Client> optionalClient = clientRepository.findById(idClient);
        if (optionalClient.isPresent()) {
            return Optional.of(new ClientDto(optionalClient.get().getId(), optionalClient.get().getNom(),
                    optionalClient.get().getPreNom(), optionalClient.get().getAdresse(), optionalClient.get().getTel(),
                    optionalClient.get().getConseiller(), optionalClient.get().getCompteCourant(), optionalClient.get().getCompteEpargne()));
        } else {
            return Optional.empty();
        }
    }

    public boolean updateClientById(Optional<ClientDto> clientDto) {
        Optional<ClientDto> optionalClientDto = findClientById(clientDto.get().getId());
        Optional<Client> optionalClient = Optional.of(new Client());
        if (optionalClientDto.isPresent()) {

            optionalClientDto.get().setId(clientDto.get().getId());
            optionalClientDto.get().setNom(clientDto.get().getNom());
            optionalClientDto.get().setPreNom(clientDto.get().getPreNom());
            optionalClientDto.get().setAdresse(clientDto.get().getAdresse());
            optionalClientDto.get().setTel(clientDto.get().getTel());

            optionalClient.get().setId(optionalClientDto.get().getId());
            optionalClient.get().setNom(optionalClientDto.get().getNom());
            optionalClient.get().setPreNom(optionalClientDto.get().getPreNom());
            optionalClient.get().setAdresse(optionalClientDto.get().getAdresse());
            optionalClient.get().setTel(optionalClientDto.get().getTel());

            clientRepository.save(optionalClient.get());
            return true;
        } else {
            return false;
        }

    }

    public boolean deleteClient(Long idClient) {
        Boolean resultat = clientRepository.existsById(idClient);
        Optional<Client> optionalClient = Optional.of(clientRepository.getById(idClient));

        if (resultat) {
            // Supprimer le compte courant en même temps
            if(optionalClient.get().getCompteCourant() !=null) {
                compteCourantRepository.delete(clientRepository.getById(idClient).getCompteCourant());
            }
            clientRepository.deleteById(idClient);

            return true;

        } else {
            return false;
        }

    }

    public boolean virement(Long idCompteDebiteur, Long idCompteCrediteur, Long montantVirement) {

        boolean soustractionMontant = compteService.soustractionSoldeByTypeCompte(idCompteDebiteur,montantVirement);

        boolean ajoutMontant = compteService.AjoutSoldeByTypeCompte(idCompteCrediteur,montantVirement);



        return (soustractionMontant && ajoutMontant);

    }

}
