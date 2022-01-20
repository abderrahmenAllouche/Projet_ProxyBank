package fr.poei.open.ProxyBanque.controllers;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.poei.open.ProxyBanque.dtos.CompteCourantDto;
import fr.poei.open.ProxyBanque.dtos.ResponseBodyDto;
import fr.poei.open.ProxyBanque.entities.CompteCourant;
import fr.poei.open.ProxyBanque.services.CompteCourantService;


@RestController
@RequestMapping("/ProxyBank")
public class CompteCourantController {

    private String regexInteger = "[0-9]+";

    @Autowired
    CompteCourantService compteCourantService;



    @GetMapping("/CompteCourant")
    public List<CompteCourant> findAll() {

        return compteCourantService.findAllCompteCourant();

    }

    @GetMapping("/CompteCourant/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String id) {
        if(id.matches(regexInteger)) {
            Optional<CompteCourantDto> optionalCompteCourantDto = compteCourantService.findByIdCompteCourant(Long.parseLong(id));
            if (optionalCompteCourantDto.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(optionalCompteCourantDto.get());
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'entité avec l'ID : " + id +" n'existe pas"));
            }
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'ID saisi est invalide !"));
        }

    }

    @PutMapping("/CompteCourant/{id}")
    @ResponseBody
    public ResponseEntity<?> modifierById(@RequestBody CompteCourantDto compteCourantDto, @PathVariable("id") String id) {
        if(id.matches(regexInteger)) {
            compteCourantDto.setId(Long.parseLong(id));
            if(compteCourantService.verificationSiIdExiste(Long.parseLong(id))) {
                boolean resultat = compteCourantService.modifierCompteCourant(Optional.of(compteCourantDto));
                if(resultat) {
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBodyDto("Le solde a été modifié avec succès"));
                }else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Une erreur est survenue lors de la modification !"));
                }
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id n'existe pas"));
            }
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'Id saisi est invalide"));
        }

    }

}

