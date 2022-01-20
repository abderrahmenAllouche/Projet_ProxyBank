package fr.poei.open.ProxyBanque.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import fr.poei.open.ProxyBanque.dtos.ClientDto;
import fr.poei.open.ProxyBanque.dtos.CompteEpargneDto;
import fr.poei.open.ProxyBanque.entities.Client;
import fr.poei.open.ProxyBanque.entities.CompteEpargne;
import fr.poei.open.ProxyBanque.repositories.ClientRepository;
import fr.poei.open.ProxyBanque.repositories.CompteEpargneRepository;

@Service
public class CompteEpargneService {

    @Autowired
    CompteEpargneRepository compteEpargneRepository;

    @Autowired
    ClientRepository clientRepository;

    @Lazy // Pour résoudre la dépendance circulaire lors de la création des Beans Bean A → Bean B → Bean A
    @Autowired
    ClientService clientService;

    public Boolean verificationSiIdExiste(Long id) {
        return compteEpargneRepository.existsById(id);

    }

    public boolean creerCompteEpargne(Optional<CompteEpargneDto> optionalCompteEpargneDto,	Optional<ClientDto> optionalClientDto) {

        Optional<Client> optionalClient = clientRepository.findById(optionalClientDto.get().getId());

        Optional<CompteEpargne> optionalCompteEpargne = Optional.of(new CompteEpargne(optionalClient.get(), optionalCompteEpargneDto.get().getSolde(), optionalCompteEpargneDto.get().getTaux()));

        String numCompte = Long.toString(optionalClient.get().getId()) + Integer.toString(LocalDate.now().getYear()) +Integer.toString(LocalDate.now().getMonthValue())+ Integer.toString(LocalDate.now().getDayOfMonth());
        optionalCompteEpargne.get().setNumCompte(numCompte);
        optionalCompteEpargne.get().setDate(LocalDate.now());

        if (optionalCompteEpargne.isPresent()) {
            optionalCompteEpargne = Optional.of(compteEpargneRepository.save(optionalCompteEpargne.get()));
            optionalClient.get().setCompteEpargne(optionalCompteEpargne.get());
            clientRepository.save(optionalClient.get());
            if (compteEpargneRepository.findById(optionalCompteEpargne.get().getId()).isPresent()) {

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public Optional<List<CompteEpargneDto>> findAllCompteEpargne() {
        List<CompteEpargneDto> listCompteEpargneDto = new ArrayList<>();

        Optional<List<CompteEpargne>> optionalCompteEpargne = Optional.of(compteEpargneRepository.findAll());
        if (optionalCompteEpargne.isPresent()) {
            if (optionalCompteEpargne.get().isEmpty()) {
                return Optional.of(listCompteEpargneDto);
            } else {
                for (CompteEpargne compteEpargne : optionalCompteEpargne.get()) {
                    listCompteEpargneDto.add(new CompteEpargneDto(compteEpargne.getId(), compteEpargne.getClient(),
                            compteEpargne.getNumCompte(), compteEpargne.getSolde(), compteEpargne.getDate(),
                            compteEpargne.getTaux()));
                }
            }
        }

        return Optional.of(listCompteEpargneDto);

    }

    public Optional<CompteEpargneDto> findByIdCompteEpargne(Long idClient) {
        Optional<CompteEpargne> optionalCompteEpargne = compteEpargneRepository.findById(idClient);
        if (optionalCompteEpargne.isPresent()) {
            return Optional.of(
                    new CompteEpargneDto(optionalCompteEpargne.get().getId(), optionalCompteEpargne.get().getClient(),
                            optionalCompteEpargne.get().getNumCompte(), optionalCompteEpargne.get().getSolde(),
                            optionalCompteEpargne.get().getDate(), optionalCompteEpargne.get().getTaux()));
        } else {
            return Optional.empty();
        }

    }

    public Boolean modifierCompteEpargne(Optional<CompteEpargneDto> compteEpargneDto) {
        Optional<CompteEpargne> optionalCompteEpargne = compteEpargneRepository.findById(compteEpargneDto.get().getId());


        if (optionalCompteEpargne.isPresent()) {
            if(compteEpargneDto.get().getSolde() != null) {
                optionalCompteEpargne.get().setSolde(compteEpargneDto.get().getSolde());
            }
            if(compteEpargneDto.get().getTaux() != null){
                optionalCompteEpargne.get().setTaux(compteEpargneDto.get().getTaux());
            }

            compteEpargneRepository.save(optionalCompteEpargne.get());
            return true;
        } else {
            return false;
        }

    }

    public boolean deleteCompteEpargne(Long idClient) {


        Optional<Client> optionalClient = clientRepository.findById(idClient);
        Long idCompte = optionalClient.get().getCompteEpargne().getId();

        optionalClient.get().setCompteEpargne(null);
        clientRepository.save(optionalClient.get());

        compteEpargneRepository.deleteById(idCompte);

        return true;


    }

    public Boolean VerifSoldeCompteSuffisant(CompteEpargneDto compteEpargneDto, Long montantVirement) {

        Boolean result;
        if ((compteEpargneDto.getSolde() - montantVirement) >= 0) {
            result = true;
        } else {
            result = false;
        }

        return result;

    }

}
