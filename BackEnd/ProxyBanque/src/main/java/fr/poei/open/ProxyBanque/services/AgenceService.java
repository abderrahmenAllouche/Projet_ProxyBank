package fr.poei.open.ProxyBanque.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.poei.open.ProxyBanque.dtos.AgenceDtos;
import fr.poei.open.ProxyBanque.dtos.ClientSoldeVM;
import fr.poei.open.ProxyBanque.entities.Agence;
import fr.poei.open.ProxyBanque.entities.Client;
import fr.poei.open.ProxyBanque.entities.Conseiller;
import fr.poei.open.ProxyBanque.entities.Gerant;
import fr.poei.open.ProxyBanque.repositories.AgenceRepository;
import fr.poei.open.ProxyBanque.repositories.GerantRepository;

@Service
public class AgenceService {

    @Autowired
    AgenceRepository agenceRepository;
    @Autowired
    GerantRepository gerantRepository;

    public Optional<List<AgenceDtos>> findAllAgence() {
        List<AgenceDtos> listAgenceDtos = new ArrayList<>();
        Optional<List<Agence>> optionalAgence = Optional.of(agenceRepository.findAll());
        if (optionalAgence.isPresent()) {
            if (optionalAgence.get().isEmpty()) {
                return Optional.of(listAgenceDtos);
            } else {
                for (Agence agence : optionalAgence.get()) {
                    listAgenceDtos.add(new AgenceDtos(agence.getId(), agence.getDateCreation(), agence.getNumeroIdentification(), agence.getGerant(), agence.getNom()));
                }
            }
        }
        return Optional.of(listAgenceDtos);
    }

    public Boolean creerAgence(Optional<AgenceDtos> optionalAgenceDtos) {
        Optional<Agence> optionalAgence = Optional.of(new Agence());
        optionalAgence.get().setNom(optionalAgenceDtos.get().getNom());
        if(optionalAgenceDtos.get().getGerant()!=null){
            optionalAgence.get().setGerant(optionalAgenceDtos.get().getGerant());
        }else{
            optionalAgence.get().setGerant(null);
        }
        if (optionalAgence.isPresent()) {
            optionalAgence = Optional.of(agenceRepository.save(optionalAgence.get()));
            optionalAgence.get().setDateCreation(LocalDate.now());
            optionalAgence.get().setNumeroIdentification(null);
            optionalAgence = Optional.of(agenceRepository.save(optionalAgence.get()));
            Optional<Agence> agence = agenceRepository.findById(optionalAgence.get().getId());
            if (agenceRepository.findById(agence.get().getId()).isPresent()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public Boolean supprimerAgence(Integer id) {
        Boolean resultat = agenceRepository.existsById(id);
        if (resultat) {
            agenceRepository.deleteById(id);
            resultat = agenceRepository.existsById(id);
            if (!resultat) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public Optional<AgenceDtos> findAgenceById(Integer id) {
        Optional<Agence> optionalAgence = agenceRepository.findById(id);
       if (optionalAgence.isPresent()) {
            return Optional.of(new AgenceDtos(optionalAgence.get().getId(), optionalAgence.get().getDateCreation(), optionalAgence.get().getNumeroIdentification(), optionalAgence.get().getGerant(), optionalAgence.get().getNom()));

        } else {
            return Optional.empty();
        }

    }

    public boolean modifierAgenceById(Optional<AgenceDtos> agenceDtos, Integer id) {
        Optional<AgenceDtos> optionalAgenceDtos = findAgenceById(id);
        Optional<Agence> optionalAgence = Optional.of(new Agence());
        if (optionalAgenceDtos.isPresent()) {

            optionalAgence.get().setId(optionalAgenceDtos.get().getId());
            optionalAgence.get().setNom(agenceDtos.get().getNom());
            optionalAgence.get().setGerant(optionalAgenceDtos.get().getGerant());
            optionalAgence.get().setDateCreation(optionalAgenceDtos.get().getDateCreation());
            optionalAgence.get().setNumeroIdentification(optionalAgenceDtos.get().getNumeroIdentification());

            agenceRepository.save(optionalAgence.get());
            return true;
        } else {
            return false;
        }

    }

    public boolean verificationSiIdExiste(Integer id) {
        return agenceRepository.existsById(id);
    }

    public boolean verificationSiGerantExiste(Integer id_gerant, Integer id_agence) {
        Optional<Agence> optionalAgence = agenceRepository.findById(id_agence);
        Optional<Gerant> optionalGerant = gerantRepository.findById(id_gerant);
        if (optionalAgence.get().getGerant() != null) {

            if (id_gerant == optionalAgence.get().getGerant().getId()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }


    }

    public List<ClientSoldeVM> audit(Integer idAgence) {
        List<ClientSoldeVM> listClientSoldeVM=new ArrayList<ClientSoldeVM>();
        Optional<AgenceDtos> optionalAgenceDtos = findAgenceById(idAgence);

        if (optionalAgenceDtos.get().getGerant() != null) {
            if (optionalAgenceDtos.get().getGerant().getConseillers().size() != 0) {
                for (Conseiller conseiller : optionalAgenceDtos.get().getGerant().getConseillers()) {

                    if (conseiller.getClients().size() != 0) {
                        for (Client client : conseiller.getClients()) {

                            if(client.getCompteCourant().getSolde()<-5000) {
                                ClientSoldeVM clientSoldeVM = new ClientSoldeVM();
                                clientSoldeVM.setId(client.getId());
                                clientSoldeVM.setNom(client.getNom());
                                clientSoldeVM.setConseiller_id(conseiller.getId());
                                clientSoldeVM.setSolde(client.getCompteCourant().getSolde());
                                listClientSoldeVM.add(clientSoldeVM);
                            }

                        }
                    }
                }
            }
        }
        return listClientSoldeVM;

    }


}






