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

import fr.poei.open.ProxyBanque.dtos.CompteEpargneDto;
import fr.poei.open.ProxyBanque.dtos.ResponseBodyDto;
import fr.poei.open.ProxyBanque.services.CompteEpargneService;

@RestController
@RequestMapping("/ProxyBank")
public class CompteEpargneController {

    private String regexInteger = "[0-9]+";

    @Autowired
    CompteEpargneService compteEpargneService;

//	@PostMapping("/CompteEpargne")
//	@ResponseBodyDto
//	public ResponseEntity<?> creerCompteEpargne(@RequestBody CompteEpargneDto compteEpargneDto) {
//		if (compteEpargneDto.getId() == null) {
//			boolean resultat = compteEpargneService.creerCompteEpargne(Optional.of(compteEpargneDto));
//			if (resultat) {
//				return ResponseEntity.status(HttpStatus.ACCEPTED).body("Le compte Epargne a été créé avec succès !");
//			} else {
//				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//						.body(" Une erreur est survenue lors de la création");
//			}
//
//		} else {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'id est généré automatiquement! ");
//		}
//
//	}

    @GetMapping("/CompteEpargne")
    @ResponseBody
    public ResponseEntity<?> findAll() {
        Optional<List<CompteEpargneDto>> optionalCompteEpargneDto = compteEpargneService.findAllCompteEpargne();
        if (optionalCompteEpargneDto.isPresent()) {
            if (optionalCompteEpargneDto.get().isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseBodyDto("La liste est vide"));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(optionalCompteEpargneDto.get());
            }
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseBodyDto("La liste est vide"));
        }

    }

    @GetMapping("/CompteEpargne/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String id) {
        if (id.matches(regexInteger)) {
            Optional<CompteEpargneDto> optionalCompteEpargneDto = compteEpargneService
                    .findByIdCompteEpargne(Long.parseLong(id));
            if (optionalCompteEpargneDto.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(optionalCompteEpargneDto.get());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'entité avec l'ID : " + id + " n'existe pas"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'ID saisi est invalide !"));
        }

    }

    @PutMapping("/CompteEpargne/{id}")
    @ResponseBody
    public ResponseEntity<?> modifierByIdCompteEpargne(@RequestBody CompteEpargneDto compteEpargneDto,@PathVariable("id") String id) {
        if (id.matches(regexInteger)) {
            compteEpargneDto.setId(Long.parseLong(id));
            if (compteEpargneService.verificationSiIdExiste(compteEpargneDto.getId())) {
                boolean resultat = compteEpargneService.modifierCompteEpargne(Optional.of(compteEpargneDto));
                if (resultat) {
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBodyDto("Le compte a été modifié avec succès"));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Une erreur est survenue lors de la modification !"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id n'existe pas"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'Id saisi est invalide"));
        }

    }


}
