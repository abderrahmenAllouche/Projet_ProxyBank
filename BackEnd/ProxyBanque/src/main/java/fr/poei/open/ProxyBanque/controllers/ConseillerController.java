package fr.poei.open.proxybanque.controllers;

import fr.poei.open.proxybanque.dtos.ClientVM;
import fr.poei.open.proxybanque.dtos.ConseillerDto;
import fr.poei.open.proxybanque.dtos.ResponseBodyDto;
import fr.poei.open.proxybanque.services.ClientService;
import fr.poei.open.proxybanque.services.ConseillerService;
import fr.poei.open.proxybanque.services.GerantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/ProxyBank")
public class ConseillerController {

    private String regexInteger = "[0-9]+";

    @Autowired
    ConseillerService conseillerService;

    @Autowired
    ClientService clientService;

    @Autowired
    GerantService gerantService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/conseiller")
    @ResponseBody
    public ResponseEntity<?> findAllConseiller(){
        Optional<List<ConseillerDto>> optionalConseillerDto = conseillerService.findAllConseiller();
        if (optionalConseillerDto.isPresent()){
            if (optionalConseillerDto.get().isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("La liste est vide"));
            }else{
                return ResponseEntity.status(HttpStatus.OK).body(optionalConseillerDto.get());
            }
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("La liste est vide"));
        }
    }

    @PreAuthorize("hasAnyAuthority('GERANT','ADMIN')")
    @GetMapping("/conseiller/disponible/{id}")
    @ResponseBody
    public ResponseEntity<?> findConseillerDisponible(@PathVariable("id") String id){
        if(id.matches(regexInteger)){
            if(gerantService.verificationSiIdExiste(Integer.parseInt(id))){
                Optional<List<ConseillerDto>> optionalConseillerDto = conseillerService.findConseillerDisponible(Integer.parseInt(id));
                if (optionalConseillerDto.isPresent()){
                    if (optionalConseillerDto.get().isEmpty()){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Il y a aucun conseillers disponible"));
                    }else{
                        return ResponseEntity.status(HttpStatus.OK).body(optionalConseillerDto.get());
                    }
                }else{
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Il y a aucun conseillers disponible"));
                }
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("l'id du gerant est introuvable"));
            }
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Id invalide "));
        }
    }

    @PreAuthorize("hasAnyAuthority('GERANT','ADMIN')")
    @PostMapping("/conseiller")
    public ResponseEntity<?> creerConseiller(@RequestBody ConseillerDto conseillerDto){
        if(conseillerDto.getId()==null) {
            if (conseillerDto.getClients().isEmpty()||conseillerDto.getClients()==null) {
                boolean resultat = conseillerService.creerConseillerSansClient(Optional.of(conseillerDto));
                if (resultat) {
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBodyDto("L'entit?? ?? ??t?? cr????e avec succ??s !"));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id du gerant n'existe pas"));
                }
            } else {
                boolean resultat = conseillerService.creerConseiller(Optional.of(conseillerDto));
                if (resultat) {
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBodyDto("L'entit?? ?? ??t?? cr????e avec succ??s !"));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id du gerant n'existe pas"));
                }
            }
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id est g??n??r?? automatiquement"));
        }

    }

    @PreAuthorize("hasAnyAuthority('GERANT','ADMIN')")
    @DeleteMapping("/conseiller/{id}")
    @Transactional
    public ResponseEntity<?>  supprimerConseiller(@PathVariable("id") String id){
        if(id.matches(regexInteger)){
            Boolean resultat = conseillerService.verificationSiIdExiste(Long.parseLong(id));
            if (resultat){
                Boolean controlList = conseillerService.verifieTailleListClientNull(Long.parseLong(id));
                if(controlList){
                    Boolean suppression = conseillerService.supprimerConseiller(Long.parseLong(id));
                    if (suppression){
                        return ResponseEntity.status(HttpStatus.OK).body(new ResponseBodyDto("L'entit?? avec l'id : "+id+" a ??t?? supprim??e avec succ??s"));
                    }else {
                        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Une erreur une survenue !"));
                    }
                }else{
                    return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Impossible de supprimer le conseiller. \nAssurez-vous d'avoir d??sassign?? tous ses clients avant sa suppression"));
                }

            }else{
                return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'entit?? avec l'id: "+id+" n'existe pas"));
            }
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id saisie est invalide !"));
        }
    }

    @PreAuthorize("hasAnyAuthority('GERANT','ADMIN')")
    @GetMapping("/conseiller/{id}")
    @ResponseBody
    public ResponseEntity<?> findConseillerById(@PathVariable("id") String id){
        if(id.matches(regexInteger)){
            Optional<ConseillerDto> optionalConseillerDto = conseillerService.findConseillerById(Long.parseLong(id));
            if (optionalConseillerDto.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(optionalConseillerDto.get());
            }else{
                return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'entit?? avec l'id:"+id+" n'existe pas"));
            }
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id saisie est invalide !"));
        }
    }

    @PreAuthorize("hasAnyAuthority('GERANT','ADMIN')")
    @PutMapping("/conseiller/{id}")
    @ResponseBody
    public ResponseEntity<?> updateConseiller(@RequestBody ConseillerDto conseillerDto,@PathVariable("id") String id){
        if(id.matches(regexInteger)){
            if (conseillerService.verificationSiIdExiste(Long.parseLong(id))){
                boolean resultat = conseillerService.modifierConseillerById(Optional.of(conseillerDto),Long.parseLong(id));
                if (resultat){
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBodyDto("L'entit?? ?? ??t?? modifi?? avec succ??s !"));
                }else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Une erreur est survenue lors de la modification !"));
                }
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id n'existe pas"));
            }
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id saisie est invalide !"));
        }
    }

    @PreAuthorize("hasAnyAuthority('CONSEILLER', 'GERANT','ADMIN')")
    @GetMapping("/conseiller/{id}/clients")
    @ResponseBody
    public ResponseEntity<?> findClientsByConseillerId(@PathVariable("id") String id){
        if(id.matches(regexInteger)){
            if(conseillerService.verificationSiIdExiste(Long.parseLong(id))){
                Optional<List<ClientVM>> optionalClientVMListByConseillerId = Optional.of(conseillerService.findClientsByConseillerId(Long.parseLong(id)));
                if (optionalClientVMListByConseillerId.isPresent()){
                    if (optionalClientVMListByConseillerId.get().isEmpty()){
                        return ResponseEntity.status(HttpStatus.OK).body(new ResponseBodyDto("Ce conseiller n'a aucun clients ?? sa charge"));
                    }else{
                        return ResponseEntity.status(HttpStatus.OK).body(optionalClientVMListByConseillerId.get());
                    }
                }else{
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseBodyDto("Ce conseiller n'a aucun clients ?? sa charge"));
                }
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id n'existe pas"));
            }
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id saisie est invalide !"));
        }
    }

    @PreAuthorize("hasAnyAuthority('GERANT','ADMIN')")
    @PutMapping("/conseiller/{id_conseiller}/client/{id_client}")
    @ResponseBody
    public ResponseEntity<?> assignerConseiller(@PathVariable("id_conseiller") String id_conseiller, @PathVariable("id_client") String id_client){
        if(id_conseiller.matches(regexInteger)&&id_client.matches(regexInteger)) {
            if (conseillerService.verificationSiIdExiste(Long.parseLong(id_conseiller))) {
                if (clientService.verficationSiIdExiste(Long.parseLong(id_client))) {
                    boolean resultat = this.conseillerService.assigneClientConseiller(Long.parseLong(id_client), Long.parseLong(id_conseiller));
                    if (resultat){
                        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBodyDto("Le conseiller : "+id_conseiller+" prend ?? sa charge le client : "+id_client));
                    }else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Erreur : Le conseiller peux avoir au maximum 10 clients ?? sa charge !"));
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id du client n'existe pas !"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id du conseiller n'existe pas !"));
            }
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'une des id saisie est invalide !'"));
        }
    }

    @PreAuthorize("hasAnyAuthority('GERANT','ADMIN')")
    @DeleteMapping("/conseiller/{id_conseiller}/client/{id_client}")
    @ResponseBody
    public ResponseEntity<?> desassignerConseiller(@PathVariable("id_conseiller") String id_conseiller, @PathVariable("id_client") String id_client){
        if(id_conseiller.matches(regexInteger)&&id_client.matches(regexInteger)) {
            if (conseillerService.verificationSiIdExiste(Long.parseLong(id_conseiller))) {
                if (clientService.verficationSiIdExiste(Long.parseLong(id_client))) {
                    boolean resultat = this.conseillerService.desassigneClientConseiller(Long.parseLong(id_client), Long.parseLong(id_conseiller));
                    if (resultat){
                        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseBodyDto("Le client : "+id_client+" ne poss??de plus de conseiller"));
                    }else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Erreur : Le conseiller : "+id_conseiller+" n'a pas ?? sa charge le client : "+id_client));
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id du client n'existe pas !"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id du conseiller n'existe pas !"));
            }
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'une des id saisie est invalide !'"));
        }
    }

    @PreAuthorize("hasAnyAuthority('CONSEILLER', 'GERANT','ADMIN')")
    @GetMapping("/conseiller/{id_conseiller}/client/{id_client}")
    @ResponseBody
    public ResponseEntity<?> findClientIDByConseillerID(@PathVariable("id_conseiller") String id_conseiller, @PathVariable("id_client") String id_client){
        if(id_conseiller.matches(regexInteger)&&id_client.matches(regexInteger)) {
            if (conseillerService.verificationSiIdExiste(Long.parseLong(id_conseiller))) {
                if (clientService.verficationSiIdExiste(Long.parseLong(id_client))) {
                    Optional<ClientVM> clientDto = conseillerService.findClientIdByConseillerID(Long.parseLong(id_client), Long.parseLong(id_conseiller));
                    if(clientDto.isPresent()){
                        if (clientDto.isEmpty()){
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Erreur : Le client : "+id_client+" est vide !"));
                        }else {
                            return ResponseEntity.status(HttpStatus.ACCEPTED).body(clientDto.get());
                        }
                    }else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("Erreur : Le conseiller : "+id_conseiller+" n'a pas ?? sa charge le client : "+id_client));
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id du client n'existe pas !"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'id du conseiller n'existe pas !"));
            }
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBodyDto("L'une des id saisie est invalide !'"));
        }
    }

    @PreAuthorize("hasAnyAuthority('CONSEILLER', 'GERANT','ADMIN')")
    @GetMapping("/conseiller/utilisateur/{id_utilisateur}")
    public ResponseEntity<?> findConseillerByUtilisateur(@PathVariable("id_utilisateur") String id_utilisateur) {
        return ResponseEntity.status(HttpStatus.OK).body(this.conseillerService.findConseillerByUtilisateurId(id_utilisateur).get());
    }





}
