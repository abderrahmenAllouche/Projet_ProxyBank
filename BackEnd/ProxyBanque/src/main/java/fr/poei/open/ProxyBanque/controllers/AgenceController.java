package fr.poei.open.proxybanque.controllers;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import fr.poei.open.proxybanque.dtos.AgenceDtos;
import fr.poei.open.proxybanque.dtos.ClientSoldeVM;
import fr.poei.open.proxybanque.dtos.ResponseBodyDto;
import fr.poei.open.proxybanque.services.AgenceService;
@CrossOrigin
@RestController
@RequestMapping("/ProxyBank")
public class AgenceController {
    private String regexInteger = "[0-9]+";
    

    @Autowired
    AgenceService agenceService;

    @GetMapping("/Agence")
    @ResponseBody
    public ResponseEntity<?> findAllAgence() {
        Optional<List<AgenceDtos>> optionalAgenceDtos = agenceService.findAllAgence();
        if (optionalAgenceDtos.isPresent()) {
            if (optionalAgenceDtos.get().isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseBodyDto("La liste est vide"));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(optionalAgenceDtos.get());
            }
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseBodyDto("La liste est vide"));
        }

    }

    @PostMapping("/Agence")
    public ResponseEntity<?> creerAgence(@RequestBody AgenceDtos agenceDtos) {
        if (agenceDtos.getId() == null) {
            Boolean resultat = agenceService.creerAgence(Optional.of(agenceDtos));
            if (resultat) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBodyDto("L'entité a été créer avec succès!"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Une erreur est survenue lors de la creation!"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id existe déja"));
        }
    }

    @DeleteMapping("/Agence/{id}")
    @Transactional
    public ResponseEntity<?> supprimerAgence(@PathVariable("id") String id) {
        if (id.matches(regexInteger)) {
            Boolean resultat = agenceService.supprimerAgence(Integer.parseInt(id));
            if (resultat) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseBodyDto("L'entité avec l'id:" + id + "a été supprimée avec succès"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'entité avec l'id :" + id + "n'existe pas"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id saisie est invalide!"));
        }
    }

    @GetMapping("/Agence/{id}")
    @ResponseBody
    public ResponseEntity<?> findAgenceById(@PathVariable("id") String id) {
        if (id.matches(regexInteger)) {
            Optional<AgenceDtos> optionalAgenceDtos = agenceService.findAgenceById(Integer.parseInt(id));
            if (optionalAgenceDtos.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(optionalAgenceDtos.get());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'entité avec l'id :" + id + "n'existe pas"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id saisie est invalide!"));
        }

    }

    @PutMapping("/Agence/{id}")
    @ResponseBody
    public ResponseEntity<?> modifierAgence(@RequestBody AgenceDtos agenceDtos, @PathVariable("id") String id) {
        if (id.matches(regexInteger)) {
            if (agenceService.verificationSiIdExiste(Integer.parseInt(id))) {
                Boolean resultat = agenceService.modifierAgenceById(Optional.of(agenceDtos),  Integer.parseInt(id));
                if (resultat) {
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBodyDto("L'entité avec l'id:" + id + "a été modifié avec succès"));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Une erreur s'est produite, l'id du gerant est invalide"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id n'existe pas"));
            }

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id saisie est invalide!"));
        }

    }

    @GetMapping("/Audit/{id}")
    @ResponseBody
    public ResponseEntity<?> creerAudit(@PathVariable("id") String id) {
        if (id.matches(regexInteger)) {
            if (agenceService.verificationSiIdExiste(Integer.parseInt(id))) {
                Optional<List<ClientSoldeVM>> optionalListClientSoldeVM = Optional.of(agenceService.audit(Integer.parseInt(id)));
                if(optionalListClientSoldeVM.isPresent()) {
                    if(optionalListClientSoldeVM.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'audit s'est realise,aucun client n'a ete trouve"));

                    }else {
                        return ResponseEntity.status(HttpStatus.OK).body(optionalListClientSoldeVM.get());

                    }

                }else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'audit s'est realise,aucun client n'a un solde <-5000!"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id n'existe pas"));
            }

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id saisie est invalide!"));
        }
    }
}



