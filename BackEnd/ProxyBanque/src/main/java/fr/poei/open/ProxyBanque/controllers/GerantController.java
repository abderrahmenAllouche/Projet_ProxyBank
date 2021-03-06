package fr.poei.open.proxybanque.controllers;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import fr.poei.open.proxybanque.dtos.ConseillerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import fr.poei.open.proxybanque.dtos.ConseillerVM;
import fr.poei.open.proxybanque.dtos.GerantDtos;
import fr.poei.open.proxybanque.dtos.ResponseBodyDto;
import fr.poei.open.proxybanque.services.AgenceService;
import fr.poei.open.proxybanque.services.ConseillerService;
import fr.poei.open.proxybanque.services.GerantService;

@RestController
@CrossOrigin
@RequestMapping("/ProxyBank")
public class GerantController {
    private String regexInteger = "[0-9]+";

    @Autowired
    GerantService gerantService;

    @Autowired
    ConseillerService conseillerService;
    @Autowired
    AgenceService agenceService;

    @GetMapping("/Gerant")
    @ResponseBody
    public ResponseEntity<?> findAllGerant() {
        Optional<List<GerantDtos>> optionalGerantDtos = gerantService.findAllGerant();
        if (optionalGerantDtos.isPresent()) {
            if (optionalGerantDtos.get().isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseBodyDto("La liste est vide"));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(optionalGerantDtos.get());
            }
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseBodyDto("La liste est vide"));
        }

    }

    @PostMapping("/Gerant")
    public ResponseEntity<?> creerGerant(@RequestBody GerantDtos gerantDtos) {
        if (gerantDtos.getId() == null) {
            Boolean resultat = gerantService.creerGerant(Optional.of(gerantDtos));
            if (resultat) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBodyDto("L'entit?? a ??t?? cr??er avec succ??s!"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Une erreur est survenue lors de la creation!"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id existe d??ja"));
        }
    }

    @DeleteMapping("/Gerant/{id}")
    @Transactional
    public ResponseEntity<?> supprimerGerant(@PathVariable("id") String id) {
        if (id.matches(regexInteger)) {
            Boolean resultat = gerantService.supprimerGerant(Integer.parseInt(id));
            if (resultat) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseBodyDto("L'entit?? avec l'id:" + id + "a ??t?? supprim??e avec succ??s"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'entit?? avec l'id :" + id + "n'existe pas"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id saisie est invalide!"));
        }
    }

    @GetMapping("/Gerant/{id}")
    @ResponseBody
    public ResponseEntity<?> findGerantById(@PathVariable("id") String id) {
        if (id.matches(regexInteger)) {
            Optional<GerantDtos> optionalGerantDtos = gerantService.findGerantById(Integer.parseInt(id));
            if (optionalGerantDtos.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(optionalGerantDtos.get());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'entit?? avec l'id :" + id + "n'existe pas"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id saisie est invalide!"));
        }
    }

    @PutMapping("/Gerant/{id}")
    @ResponseBody
    public ResponseEntity<?> modifierGerant(@RequestBody GerantDtos gerantDtos, @PathVariable("id") String id) {
        if (id.matches(regexInteger)) {
            if (gerantService.verificationSiIdExiste(Integer.parseInt(id))) {
                Boolean resultat = gerantService.modifierGerantById(Optional.of(gerantDtos), Integer.parseInt(id));
                if (resultat) {
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBodyDto("L'entit?? avec l'id:" + id + "a ??t?? modifi?? avec succ??s"));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("l'id du gerant n'existe pas"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id n'existe pas"));
            }

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id saisie est invalide!"));
        }

    }

    @PreAuthorize("hasAnyAuthority('GERANT','ADMIN')")
    @GetMapping("/gerant/{id}/conseillers")
    @ResponseBody
    public ResponseEntity<?> findConseillersByGerantId(@PathVariable("id") String id) {
        if (id.matches(regexInteger)) {
            if (gerantService.verificationSiIdExiste(Integer.parseInt(id))) {
                Optional<List<ConseillerDto>> optionalConseillerDtoListByGerantId = Optional.of(gerantService.findConseillersByGerantId(Integer.parseInt(id)));
                if (optionalConseillerDtoListByGerantId.isPresent()) {
                    if (optionalConseillerDtoListByGerantId.get().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.OK).body(new ResponseBodyDto("Ce gerant n'a aucun conseillers ?? sa charge"));
                    } else {
                        return ResponseEntity.status(HttpStatus.OK).body(optionalConseillerDtoListByGerantId.get());
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseBodyDto("Ce gerant n'a aucun conseillers ?? sa charge"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id n'existe pas"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id saisie est invalide !"));
        }
    }

    @PutMapping("/gerant/{id_gerant}/conseiller/{id_conseiller}")
    @ResponseBody
    public ResponseEntity<?> assignerGerant(@PathVariable("id_gerant") String id_gerant, @PathVariable("id_conseiller") String id_conseiller) {
        if (id_gerant.matches(regexInteger) && id_conseiller.matches(regexInteger)) {
            if (gerantService.verificationSiIdExiste(Integer.parseInt(id_gerant))) {
                if (conseillerService.verificationSiIdExiste(Long.parseLong(id_conseiller))) {
                    boolean resultat = this.gerantService.assigneConseillerGerant(Long.parseLong(id_conseiller), Integer.parseInt(id_gerant));
                    if (resultat) {
                        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBodyDto("Le gerant : " + id_gerant + " prend ?? sa charge le conseiller : " + id_conseiller));
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Erreur : Le gerant ne prend pas ?? sa charge les conseillers  !"));
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id du conseiller n'existe pas !"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id du gerant n'existe pas !"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'une des id saisie est invalide !'"));
        }
    }

    @PutMapping("/gerant/{id_gerant}/agence/{id_agence}")
    @ResponseBody
    public ResponseEntity<?> assignerAgenceGerant(@PathVariable("id_gerant") String id_gerant, @PathVariable("id_agence") String id_agence) {
        if (id_gerant.matches(regexInteger) && id_agence.matches(regexInteger)) {
            if (gerantService.verificationSiIdExiste(Integer.parseInt(id_gerant))) {
                if (agenceService.verificationSiIdExiste(Integer.parseInt(id_agence))) {
                    boolean resultat = this.gerantService.assigneAgenceGerant(Integer.parseInt(id_agence), Integer.parseInt(id_gerant));
                    if (resultat) {
                        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBodyDto("Le gerant : " + id_gerant + " est assign?? ?? l'agence : " + id_agence));
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Erreur : Le gerant n'est pas assign??  !"));
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id de l'agence n'existe pas !"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id du gerant n'existe pas !"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'une des id saisie est invalide !'"));
        }
    }

    @DeleteMapping("/gerant/{id_gerant}/conseiller/{id_conseiller}")
    @ResponseBody
    public ResponseEntity<?> desassignerGerant(@PathVariable("id_gerant") String id_gerant, @PathVariable("id_conseiller") String id_conseiller) {
        if (id_gerant.matches(regexInteger) && id_conseiller.matches(regexInteger)) {
            if (gerantService.verificationSiIdExiste(Integer.parseInt(id_gerant))) {
                if (gerantService.verificationSiIdExiste(Integer.parseInt(id_gerant))) {
                    boolean resultat = this.gerantService.desassigneConseillerGerant(Long.parseLong(id_conseiller), Integer.parseInt(id_gerant));
                    if (resultat) {
                        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBodyDto("Le conseiller : " + id_conseiller + " ne poss??de plus de gerant"));
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Erreur : Le gerant : " + id_gerant + " n'a pas ?? sa charge leconseiller : " + id_conseiller));
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id du conseiller n'existe pas !"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id du gerant n'existe pas !"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'une des id saisie est invalide !'"));
        }
    }

    @DeleteMapping("/gerant/{id_gerant}/agence/{id_agence}")
    @ResponseBody
    public ResponseEntity<?> desassignerAgenceGerant(@PathVariable("id_gerant") String id_gerant, @PathVariable("id_agence") String id_agence) {
        if (id_gerant.matches(regexInteger) && id_agence.matches(regexInteger)) {
            if (gerantService.verificationSiIdExiste(Integer.parseInt(id_gerant))) {

                if (agenceService.verificationSiIdExiste(Integer.parseInt(id_agence))) {
                    if (agenceService.verificationSiGerantExiste(Integer.parseInt(id_gerant), Integer.parseInt(id_agence))) {
                        boolean resultat = this.gerantService.desassigneAgenceGerant(Integer.parseInt(id_agence), Integer.parseInt(id_gerant));
                        if (resultat) {
                            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBodyDto("Le gerant : " + id_gerant + " est desassign?? de l'agence : " + id_agence));

                        } else {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto(" Erreur : Le gerant est toujours assign??  !"));
                        }
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("  Le gerant n'est pas  assign?? ?? cette agence  !"));
                    }

                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id de l'agence n'existe pas !"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id du gerant n'existe pas !"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'une des id saisie est invalide !'"));
        }
    }

    @PreAuthorize("hasAnyAuthority('GERANT','ADMIN')")
    @GetMapping("/gerant/{id_gerant}/conseiller/{id_conseiller}")
    @ResponseBody
    public ResponseEntity<?> findConseillerIDByGerantID(@PathVariable("id_gerant") String id_gerant, @PathVariable("id_conseiller") String id_conseiller) {
        if (id_gerant.matches(regexInteger) && id_conseiller.matches(regexInteger)) {
            if (gerantService.verificationSiIdExiste(Integer.parseInt(id_gerant))) {
                if (conseillerService.verificationSiIdExiste(Long.parseLong(id_conseiller))) {
                    Optional<ConseillerVM> conseillerVm = gerantService.findConseillerIdByGerantID(Long.parseLong(id_conseiller), Integer.parseInt(id_gerant));
                    if (conseillerVm.isPresent()) {
                        if (conseillerVm.isEmpty()) {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Erreur : Le conseiller : " + id_conseiller + " est vide !"));
                        } else {
                            return ResponseEntity.status(HttpStatus.ACCEPTED).body(conseillerVm.get());
                        }
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Erreur : Le gerant : " + id_gerant + " n'a pas ?? sa charge le conseiller: " + id_conseiller));
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id du conseiller n'existe pas !"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id du gerant n'existe pas !"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'une des id saisie est invalide !'"));
        }
    }

    @PreAuthorize("hasAnyAuthority('GERANT','ADMIN')")
    @GetMapping("/gerant/utilisateur/{id_utilisateur}")
    public ResponseEntity<?> findGerantByUtilisateur(@PathVariable("id_utilisateur") String id_utilisateur) {
        return ResponseEntity.status(HttpStatus.OK).body(this.gerantService.findGerantByUtilisateurId(id_utilisateur).get());
    }

}


