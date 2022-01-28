package fr.poei.open.proxybanque.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.poei.open.proxybanque.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.poei.open.proxybanque.dtos.ConseillerDto;
import fr.poei.open.proxybanque.dtos.ConseillerVM;
import fr.poei.open.proxybanque.dtos.GerantDtos;
import fr.poei.open.proxybanque.entities.Agence;
import fr.poei.open.proxybanque.entities.Conseiller;
import fr.poei.open.proxybanque.entities.Gerant;
import fr.poei.open.proxybanque.repositories.AgenceRepository;
import fr.poei.open.proxybanque.repositories.ConseillerRepository;
import fr.poei.open.proxybanque.repositories.GerantRepository;

@Service
public class GerantService {

    @Autowired
    GerantRepository gerantRepository;
    @Autowired
    ConseillerRepository conseillerRepository;
    @Autowired
    ConseillerService conseillerService;
    @Autowired
    AgenceRepository agenceRepository;
    @Autowired
    AgenceService agenceService;
    @Autowired
    UtilisateurRepository utilisateurRepository;

    public Optional<List<GerantDtos>> findAllGerant() {
        List<GerantDtos> listGerantDtos = new ArrayList<>();
        Optional<List<Gerant>> optionalGerant = Optional.of(gerantRepository.findAll());
        if (optionalGerant.isPresent()) {
            if (optionalGerant.get().isEmpty()) {
                return Optional.of(listGerantDtos);
            } else {
                for (Gerant gerant : optionalGerant.get()) {
                    List<ConseillerVM> listConseillerVM = new ArrayList<>();
                    if (gerant.getConseillers().size() != 0) {
                        for (Conseiller conseiller : gerant.getConseillers()) {
                            listConseillerVM.add(new ConseillerVM(conseiller.getId(), conseiller.getNom(), gerant.getId()));
                        }

                    }
                    GerantDtos gerantDtos = new GerantDtos();
                    gerantDtos.setId(gerant.getId());
                    gerantDtos.setNom(gerant.getNom());
                    gerantDtos.setConseillers(listConseillerVM);

                    List<Agence> listAgence = agenceRepository.findAll();
                    if (listAgence.size() != 0) {
                        for (Agence agence : listAgence) {
                            if (agence.getGerant() != null) {
                                if (agence.getGerant().getId() == gerant.getId()) {
                                    gerantDtos.setIdAgence(agence.getId());
                                    break;
                                }
                            }
                            gerantDtos.setIdAgence(null);
                        }
                    }

                    listGerantDtos.add(gerantDtos);
                }
            }
        }
        return Optional.of(listGerantDtos);
    }

    public Boolean creerGerant(Optional<GerantDtos> optionalGerantDtos) {
        Optional<Gerant> optionalGerant = Optional.of(new Gerant());
        optionalGerant.get().setNom(optionalGerantDtos.get().getNom());
        List<Conseiller> listConseiller = new ArrayList<>();
        optionalGerant = Optional.of(gerantRepository.save(optionalGerant.get()));
        if (optionalGerantDtos.get().getConseillers().isEmpty()) {
            optionalGerant.get().setConseillers(listConseiller);
        } else {
            for (ConseillerVM conseillerVM : optionalGerantDtos.get().getConseillers()) {
                if (conseillerService.verificationSiIdExiste(conseillerVM.getId())) {
                    assigneConseillerGerant(conseillerVM.getId(), optionalGerant.get().getId());
                    listConseiller.add(conseillerRepository.findById(conseillerVM.getId()).get());
                }
            }
            optionalGerant.get().setConseillers(listConseiller);

        }
        if (optionalGerant.isPresent()) {
            optionalGerant = Optional.of(gerantRepository.save(optionalGerant.get()));
            return gerantRepository.findById(optionalGerant.get().getId()).isPresent();
        } else {
            return false;
        }
    }

    public Boolean supprimerGerant(Integer id) {
        Optional<Gerant> optionalGerant= gerantRepository.findById(id);
        List<Agence> optionalListAgence =agenceRepository.findAll();
        for (Agence agence : optionalListAgence) {
            if(agence.getGerant()!=null) {
                if(agence.getGerant().getId()==id) {
                    desassigneAgenceGerant(agence.getId(), id);
                }
            }
        }
        if(optionalGerant.get().getConseillers().size()!=0){
            for (Conseiller conseiller : optionalGerant.get().getConseillers()) {
                desassigneConseillerGerant(conseiller.getId(), optionalGerant.get().getId());
                if(optionalGerant.get().getConseillers().size()==0){
                    break;
                }
            }
        }

        Boolean resultat = gerantRepository.existsById(id);
        if (resultat) {
            gerantRepository.deleteById(id);
            resultat = gerantRepository.existsById(id);
            return !resultat;
        } else {
            return false;
        }
    }

    public Optional<GerantDtos> findGerantById(Integer id) {
        Optional<Gerant> optionalGerant = gerantRepository.findById(id);
        if (optionalGerant.isPresent()) {
            Optional<GerantDtos> optionalGerantDtos = Optional.of(new GerantDtos());
            optionalGerantDtos.get().setId(optionalGerant.get().getId());
            optionalGerantDtos.get().setNom(optionalGerant.get().getNom());

            List<Agence> listAgence = agenceRepository.findAll();
            if (listAgence.size() != 0) {
                for (Agence agence : listAgence) {
                    if (agence.getGerant() != null) {
                        if (agence.getGerant().getId() == optionalGerant.get().getId()) {
                            optionalGerantDtos.get().setIdAgence(agence.getId());
                            break;
                        }
                    }
                    optionalGerantDtos.get().setIdAgence(null);
                }
            }
            List<ConseillerVM> listConseillerVM = new ArrayList<ConseillerVM>();
            if (optionalGerant.get().getConseillers().size() != 0) {
                for (Conseiller conseiller : optionalGerant.get().getConseillers()) {
                    ConseillerVM conseillerVM = new ConseillerVM();
                    conseillerVM.setId(conseiller.getId());
                    conseillerVM.setNom(conseiller.getNom());
                    if (conseiller.getGerant() != null) {
                        conseillerVM.setGerant_id(conseiller.getGerant().getId());
                    } else {
                        conseillerVM.setGerant_id(null);
                    }
                    listConseillerVM.add(conseillerVM);


                }
            }
            optionalGerantDtos.get().setConseillers(listConseillerVM);
            return optionalGerantDtos;
        } else {
            return Optional.empty();
        }

    }

    public boolean modifierGerantById(Optional<GerantDtos> gerantDtos, Integer id) {
        Optional<GerantDtos> optionalGerantDtos = findGerantById(id);
        Optional<Gerant> optionalGerant = Optional.of(new Gerant());
        if (optionalGerantDtos.isPresent()) {

            optionalGerantDtos.get().setNom(gerantDtos.get().getNom());
            optionalGerantDtos.get().setConseillers(gerantDtos.get().getConseillers());

            optionalGerant.get().setId(optionalGerantDtos.get().getId());
            optionalGerant.get().setNom(optionalGerantDtos.get().getNom());

            List<Conseiller> listConseiller = new ArrayList<Conseiller>();
            if (optionalGerantDtos.get().getConseillers().size() != 0) {
                for (ConseillerVM conseillerVM : optionalGerantDtos.get().getConseillers()) {
                    Conseiller conseiller = new Conseiller();
                    conseiller.setId(conseillerVM.getId());
                    conseiller.setNom(conseillerVM.getNom());
                    if (conseillerVM.getGerant_id() != null) {
                        conseiller.setGerant(optionalGerant.get());
                    } else {
                        conseiller.setGerant(null);
                    }
                    listConseiller.add(conseiller);


                }
            }
            optionalGerant.get().setConseillers(listConseiller);
            gerantRepository.save(optionalGerant.get());
            return true;
        } else {
            return false;
        }

    }

    public boolean verificationSiIdExiste(Integer id) {
        return gerantRepository.existsById(id);
    }

    public List<ConseillerDto> findConseillersByGerantId(Integer id) {
        Optional<List<ConseillerDto>> listconseillerVDto = Optional.of(new ArrayList<ConseillerDto>());
        Optional<Gerant> gerant = gerantRepository.findById(id);
        if (gerant.isEmpty()) {
            return new ArrayList<>();
        } else {
            for (Conseiller conseiller : gerant.get().getConseillers()) {
                Optional<ConseillerDto> optionalOonseillerDto = conseillerService.findConseillerById(conseiller.getId());
                listconseillerVDto.get().add(optionalOonseillerDto.get());
            }
            return listconseillerVDto.get();
        }
    }

    public Boolean assigneConseillerGerant(Long id_conseiller, Integer id_gerant) {
        Optional<Gerant> gerant = gerantRepository.findById(id_gerant);
        Optional<Conseiller> conseiller = conseillerRepository.findById(id_conseiller);

        conseiller.get().setGerant(gerant.get());
        gerant.get().getConseillers().add(conseiller.get());
        gerantRepository.save(gerant.get());
        conseillerRepository.save(conseiller.get());
        return true;

    }

    public Boolean assigneAgenceGerant(Integer id_agence, Integer id_gerant) {
        Optional<Gerant> gerant = gerantRepository.findById(id_gerant);
        Optional<Agence> agence = agenceRepository.findById(id_agence);

        agence.get().setGerant(gerant.get());
        agenceRepository.save(agence.get());
        return true;

    }

    public Boolean desassigneConseillerGerant(Long id_conseiller, Integer id_gerant) {
        Optional<Gerant> gerant = gerantRepository.findById(id_gerant);
        Optional<Conseiller> conseiller = conseillerRepository.findById(id_conseiller);
        for (Conseiller sonconseiller : gerant.get().getConseillers()) {
            if (sonconseiller.getId() == id_conseiller) {
                conseiller.get().setGerant(null);
                gerant.get().getConseillers().remove(conseiller.get());
                gerantRepository.save(gerant.get());
                conseillerRepository.save(conseiller.get());
                return true;
            }
        }
        return false;
    }

    public Boolean desassigneAgenceGerant(Integer id_agence, Integer id_gerant) {
        Optional<Gerant> gerant = gerantRepository.findById(id_gerant);
        Optional<Agence> agence = agenceRepository.findById(id_agence);
        agence.get().setGerant(null);
        agenceRepository.save(agence.get());
        return true;

    }

    public Optional<ConseillerVM> findConseillerIdByGerantID(Long id_conseiller, Integer id_gerant) {
        Optional<Gerant> gerant = gerantRepository.findById(id_gerant);
        ConseillerVM conseillerVM = new ConseillerVM();
        for (Conseiller sonconseiller : gerant.get().getConseillers()) {
            if (sonconseiller.getId() == id_conseiller) {
                conseillerVM.setId(sonconseiller.getId());
                conseillerVM.setNom(sonconseiller.getNom());
                conseillerVM.setGerant_id(sonconseiller.getGerant().getId());
                return Optional.of(conseillerVM);
            }
        }
        return Optional.empty();
    }


    public Optional<GerantDtos> findGerantByUtilisateurId(String idUtilisateur) {
        Optional<Gerant> optionalGerant = this.gerantRepository.findGerantByUtilisateur(this.utilisateurRepository.findById(Integer.parseInt(idUtilisateur)).get());
        Optional<GerantDtos> gerantDto = findGerantById(optionalGerant.get().getId());
        return gerantDto;
    }
}
