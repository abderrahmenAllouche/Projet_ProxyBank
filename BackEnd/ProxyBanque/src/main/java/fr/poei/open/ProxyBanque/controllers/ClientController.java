package fr.poei.open.ProxyBanque.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.poei.open.ProxyBanque.dtos.CarteBancaireDto;
import fr.poei.open.ProxyBanque.dtos.ClientDto;
import fr.poei.open.ProxyBanque.dtos.CompteCourantDto;
import fr.poei.open.ProxyBanque.dtos.CompteEpargneDto;
import fr.poei.open.ProxyBanque.dtos.ResponseBodyDto;
import fr.poei.open.ProxyBanque.services.CarteBancaireService;
import fr.poei.open.ProxyBanque.services.ClientService;
import fr.poei.open.ProxyBanque.services.CompteCourantService;
import fr.poei.open.ProxyBanque.services.CompteEpargneService;
import fr.poei.open.ProxyBanque.services.CompteService;

@RestController
@RequestMapping("/ProxyBank")
public class ClientController {
    private String regexInteger = "[0-9]+";

    @Autowired
    ClientService clientService;

    @Autowired
    CompteCourantService compteCourantService;

    @Autowired
    CompteEpargneService compteEpargneService;

    @Autowired
    CompteService compteService;

    @Autowired
    CarteBancaireService carteBancaireService;

    @PostMapping("/Client")
    @ResponseBody
    public ResponseEntity<?> creerClient(@RequestBody ClientDto clientDto) {
        if (clientDto.getId() == null) {
            if (clientDto.getCompteCourant() == null) {
                boolean resultat = clientService.CreerClientSansSolde(Optional.of(clientDto));
                if (resultat) {
                    return ResponseEntity.status(HttpStatus.ACCEPTED)
                            .body(new ResponseBodyDto("Le client a été crée avec succes, le solde du compte est 0€"));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ResponseBodyDto("Une erreur est survenue lors de la créationdfgdfgdf"));
                }
            } else {
                boolean resultat = clientService.CreerClientAvecSolde(Optional.of(clientDto));
                if (resultat) {
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBodyDto("L'entité a été crée avec succes"));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Une erreur est survenue lors de la création"));
                }
            }

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id est généré automatiquement"));

        }

    }

    @GetMapping("/Client/{id}")
    @ResponseBody
    public ResponseEntity<?> findClientById(@PathVariable("id") String id) {
        if (id.matches(regexInteger)) {
            Optional<ClientDto> optionalClientDto = clientService.findClientById(Long.parseLong(id));
            if (optionalClientDto.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(optionalClientDto.get());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'entité avec l'id : " + id + " n'existe pas"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id saisi est invalide"));
        }

    }

    @GetMapping("/Client")
    @ResponseBody
    public ResponseEntity<?> findAllClient() {

        Optional<List<ClientDto>> optionalClientDto = clientService.findAllClient();
        if (optionalClientDto.isPresent()) {
            if (optionalClientDto.get().isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseBodyDto("La liste est vide"));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(optionalClientDto.get());
            }
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseBodyDto("La liste est vide"));
        }
    }

    @PutMapping("/Client/{id}")
    @ResponseBody
    private ResponseEntity<?> modifierClientById(@RequestBody ClientDto clientDto, @PathVariable("id") String id) {
        if (id.matches(regexInteger)) {
            clientDto.setId(Long.parseLong(id));
            if (clientService.verficationSiIdExiste(clientDto.getId())) {
                boolean resultat = clientService.updateClientById(Optional.of(clientDto));
                if (resultat) {
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBodyDto("L'entité a été modifiée avec succès"));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Une erreur est survenue lors de la modification"));

                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("l'Id nexiste pas"));

            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("l'id saisi est invalide"));
        }

    }

    @DeleteMapping("/Client/{id}")
    @ResponseBody
    public ResponseEntity<?> supprimerClient(@PathVariable("id") String id) {
        if (id.matches(regexInteger)) {
            Boolean resultat = clientService.deleteClient(Long.parseLong(id));
            System.out.println(resultat);

            if (resultat) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseBodyDto("L'entité avec l'id : " + id + " a été supprimée avec succès"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'entité avec l'id: " + id + " n'existe pas"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id saisie est invalide"));
        }
    }

    @PutMapping("/Virement/{idCompteDebiteur}/{idCompteCrediteur}")
    @ResponseBody
    public ResponseEntity<?> Virement(@PathVariable("idCompteDebiteur") String idCompteDebiteur,
                                      @PathVariable("idCompteCrediteur") String idCompteCrediteur,
                                      @RequestBody CompteCourantDto compteCourantDto) {

        if (idCompteDebiteur.matches(regexInteger) && idCompteCrediteur.matches(regexInteger)
                && compteCourantDto.getSolde().toString().matches(regexInteger)) {

            boolean compteDebiteurExiste = compteService.existCompteById(Long.parseLong(idCompteDebiteur));
            boolean compteCrediteurExiste = compteService.existCompteById(Long.parseLong(idCompteCrediteur));
            System.out.println(compteDebiteurExiste);
            System.out.println(compteCrediteurExiste);

            // Verifier si les id existent
            if (!compteDebiteurExiste) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("le compte debiteur n'existe pas!"));
            } else {
                if (!compteCrediteurExiste) {

                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("le compte crediteur n'existe pas!"));
                } else {
                    if (compteDebiteurExiste && compteCrediteurExiste) {

                        Boolean result = clientService.virement(Long.parseLong(idCompteDebiteur),
                                Long.parseLong(idCompteCrediteur), compteCourantDto.getSolde());

                        if (result) {
                            return ResponseEntity.status(HttpStatus.OK).body(new ResponseBodyDto("Le virement est effectué avec succès"));
                        } else {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("le solde est insuffisant"));
                        }
                    }

                }
            }

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Les informations saisies ne sont pas correctes!");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Veuillez réessayer!");
    }

    @PutMapping("/AjoutCompteEpargne/{idClient}")
    @ResponseBody
    public ResponseEntity<?> ajoutCompteEpargne(@PathVariable("idClient") String idClient,
                                                @RequestBody CompteEpargneDto compteEpargneDto) {
        ResponseEntity<ResponseBodyDto> retour;
        if (idClient.matches(regexInteger)) {
            Optional<ClientDto> optionalClientDto = clientService.findClientById(Long.parseLong(idClient));

            if (optionalClientDto.isPresent() && !optionalClientDto.isEmpty()) {

                if (optionalClientDto.get().getCompteEpargne() == null) {

                    boolean compteCree = compteEpargneService.creerCompteEpargne(Optional.of(compteEpargneDto),
                            optionalClientDto);

                    if (compteCree) {
                        retour = ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBodyDto("Le compte a ete cree avec succes"));
                    } else {
                        retour = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Un problème est survenu lors de la creation du compte d'epargne"));
                    }

                } else {
                    retour = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Le client a déjà un compte epargne, son id est : "+ optionalClientDto.get().getCompteEpargne().getId()));
                }

            } else {
                retour = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBodyDto("L'id saisi n'existe pas!"));
            }

        } else {
            retour = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ResponseBodyDto("L'id saisi n'est pas valide!"));
        }
        return retour;
    }

    @DeleteMapping("/CompteEpargne/{idClient}")
    @ResponseBody
    private ResponseEntity<?> deleteCompteEpargne(@PathVariable("idClient") String idClient) {
        if (idClient.matches(regexInteger)) {
            Optional<ClientDto> OptionalClientDto = clientService.findClientById(Long.parseLong(idClient));
            if (OptionalClientDto.isPresent() && !OptionalClientDto.isEmpty()) {

                boolean resultat = compteEpargneService.deleteCompteEpargne(Long.parseLong(idClient));

                if (resultat) {
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseBodyDto("Le compte epargne du client : " + idClient + " a été supprimée avec succès"));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Le client n'a pas de compte epargne"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'entité avec l'id:" + idClient + " n'existe pas"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id saisie est invalide"));

        }
    }

    @PutMapping("/CarteBancaire/{idClient}")
    @ResponseBody
    public ResponseEntity<?> CreerCB(@RequestBody Optional<CarteBancaireDto> optionalCarteDto,	@PathVariable("idClient") String idClient) {

        ResponseEntity<ResponseBodyDto> retour = null;

        if (idClient.matches(regexInteger)) {
            Optional<ClientDto> optionalClientDto = clientService.findClientById(Long.parseLong(idClient));
            if (optionalClientDto.isPresent() && !optionalClientDto.isEmpty()) {
                if (!optionalCarteDto.isEmpty()) {

                    if ((optionalClientDto.get().getCarteElectron() == null) || (optionalClientDto.get().getCarteVisa() == null) ){

                        if (optionalCarteDto.get().getTypeCarte().equals("Visa")) {

                            boolean resultat = carteBancaireService.creerCBVisa(optionalCarteDto, optionalClientDto);

                            if (resultat) {

                                retour = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Carte Visa créee ave succès"));
                            }
                        } else if (optionalCarteDto.get().getTypeCarte().equals("Electron")) {

                            boolean resultat = carteBancaireService.creerCBElectron(optionalCarteDto, optionalClientDto);

                            if (resultat) {

                                retour = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Carte Electron crée ave succès"));
                            }

                        } else {
                            retour = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Type de carte non reconnu"));
                        }

                    } else {
                        retour = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Client a déjà une carte"));

                    }
                } else {
                    retour = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Saisir type de carte à créer"));
                }
            }

            else {
                retour = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'ID saisi n'existe pas!"));
            }
        } else {
            retour = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'ID saisi est invalide !"));
        }
        return retour;

    }

}
