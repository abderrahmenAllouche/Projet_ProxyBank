package fr.poei.open.proxybanque.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.poei.open.proxybanque.dtos.CompteCourantDto;
import fr.poei.open.proxybanque.dtos.CompteEpargneDto;
import fr.poei.open.proxybanque.entities.Compte;
import fr.poei.open.proxybanque.repositories.CompteRepository;

@Service
public class CompteService {

    @Autowired
    CompteRepository compteRepository;

    @Autowired
    CompteCourantService compteCourantService;

    @Autowired
    CompteEpargneService compteEpargneService;

    public boolean existCompteById(Long id) {
        return compteRepository.existsById(id);
    }

    public String findTypeCompteById(Long id) {
        Optional<Compte> compte = compteRepository.findById(id);

        return compte.get().getTypeCompte();
    }

    public boolean soustractionSoldeByTypeCompte(Long id, long montantVirement) {


        if (findTypeCompteById(id).equals("compte_Courant")) {
            Optional<CompteCourantDto> optionalCompteCourantDto = compteCourantService.findByIdCompteCourant(id);

            if (compteCourantService.VerifSoldeSoldeSuffisant(optionalCompteCourantDto.get(), montantVirement)) {
                optionalCompteCourantDto.get().setSolde(optionalCompteCourantDto.get().getSolde() - montantVirement);
                compteCourantService.modifierCompteCourant(optionalCompteCourantDto);
                return true;
            } else {
                return false;
            }
        } else {
            Optional<CompteEpargneDto> optionalCompteEpargneDto = compteEpargneService.findByIdCompteEpargne(id);
            if (compteEpargneService.VerifSoldeCompteSuffisant(optionalCompteEpargneDto.get(), montantVirement)) {
                optionalCompteEpargneDto.get().setSolde(optionalCompteEpargneDto.get().getSolde() - montantVirement);
                compteEpargneService.modifierCompteEpargne(optionalCompteEpargneDto);
                return true;
            } else {
                return false;
            }

        }

    }
    public boolean AjoutSoldeByTypeCompte(Long id, long montantVirement) {

        if (findTypeCompteById(id).equals("compte_Courant")) {
            Optional<CompteCourantDto> optionalCompteCourantDto = compteCourantService.findByIdCompteCourant(id);

            optionalCompteCourantDto.get().setSolde(optionalCompteCourantDto.get().getSolde()  +montantVirement);
            compteCourantService.modifierCompteCourant(optionalCompteCourantDto);
            return true;

        } else {
            Optional<CompteEpargneDto> optionalCompteEpargneDto = compteEpargneService.findByIdCompteEpargne(id);

            optionalCompteEpargneDto.get().setSolde(optionalCompteEpargneDto.get().getSolde() + montantVirement);
            compteEpargneService.modifierCompteEpargne(optionalCompteEpargneDto);
            return true;

        }

    }
}
