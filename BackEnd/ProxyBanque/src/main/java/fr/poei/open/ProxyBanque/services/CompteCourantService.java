package fr.poei.open.ProxyBanque.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.poei.open.ProxyBanque.dtos.CompteCourantDto;
import fr.poei.open.ProxyBanque.entities.Client;
import fr.poei.open.ProxyBanque.entities.CompteCourant;
import fr.poei.open.ProxyBanque.repositories.CompteCourantRepository;

@Service
public class CompteCourantService {

    @Autowired
    CompteCourantRepository compteCourantRepository;

    public Boolean verificationSiIdExiste(Long id) {
        return compteCourantRepository.existsById(id);

    }

    public CompteCourant creerCompteCourant(CompteCourant CompteCourant, Optional<Client> optionalClient) {

        String numClient = Long.toString(optionalClient.get().getId()) + Integer.toString(LocalDate.now().getYear()) +Integer.toString(LocalDate.now().getMonthValue())+ Integer.toString(LocalDate.now().getDayOfMonth());
        CompteCourant.setNumCompte(numClient);
        CompteCourant.setDate(LocalDate.now());


        return CompteCourant;

    }

    public List<CompteCourant> findAllCompteCourant() {
        return compteCourantRepository.findAll();
    }

    public Optional<CompteCourantDto> findByIdCompteCourant(Long idCompte) {

        Optional<CompteCourant> optionalCompteCourant = compteCourantRepository.findById(idCompte);
        if (optionalCompteCourant.isPresent()) {
            return Optional.of(
                    new CompteCourantDto(optionalCompteCourant.get().getId(), optionalCompteCourant.get().getClient(),
                            optionalCompteCourant.get().getNumCompte(), optionalCompteCourant.get().getSolde(),
                            optionalCompteCourant.get().getDate(), optionalCompteCourant.get().getAutorisDecouvert()));

        } else {
            return Optional.empty();
        }

    }

    public boolean modifierCompteCourant(Optional<CompteCourantDto> CompteCourantDto ) {

        Optional<CompteCourant> optionalCompteCourant = compteCourantRepository.findById(CompteCourantDto.get().getId());



        if (optionalCompteCourant.isPresent() && !optionalCompteCourant.isEmpty()) {

            if(CompteCourantDto.get().getSolde() != null) {
                optionalCompteCourant.get().setSolde(CompteCourantDto.get().getSolde());
            }
            if(CompteCourantDto.get().getAutorisDecouvert() != null) {
                optionalCompteCourant.get().setAutorisDecouvert(CompteCourantDto.get().getAutorisDecouvert());
            }


            compteCourantRepository.save(optionalCompteCourant.get());
            return true;
        } else {
            return false;
        }

    }

    public void deleteCompte(Long idCompte) {

        compteCourantRepository.deleteById(idCompte);

    }

    public boolean VerifSoldeSoldeSuffisant(CompteCourantDto compteCourantDto, Long montantVirement) {
        Boolean result;

        if ((compteCourantDto.getSolde() - montantVirement) >= compteCourantDto.getSolde()- compteCourantDto.getAutorisDecouvert()) {
            result = true;
        } else {
            result = false;
        }

        return result;

    }

}
