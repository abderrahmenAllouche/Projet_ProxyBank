package fr.poei.open.ProxyBanque.services;

import fr.poei.open.ProxyBanque.dtos.*;
import fr.poei.open.ProxyBanque.entities.Client;
import fr.poei.open.ProxyBanque.entities.Conseiller;
import fr.poei.open.ProxyBanque.entities.Gerant;
import fr.poei.open.ProxyBanque.repositories.ClientRepository;
import fr.poei.open.ProxyBanque.repositories.ConseillerRepository;
import fr.poei.open.ProxyBanque.repositories.GerantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConseillerService {

    @Autowired
    ConseillerRepository conseillerRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    GerantRepository gerantRepository;

    @Autowired
    ClientService clienService;


    public Optional<List<ConseillerDto>> findAllConseiller(){
        List<ConseillerDto> listConseillerDto = new ArrayList<>();
        Optional<List<Conseiller>> optionalConseillers = Optional.of(conseillerRepository.findAll());
        if(optionalConseillers.isPresent()){
            if (optionalConseillers.get().isEmpty()){
                return Optional.of(listConseillerDto);
            }else{
                for(Conseiller conseiller : optionalConseillers.get()){
                    ConseillerDto conseillerDto = new ConseillerDto();
                    conseillerDto.setId(conseiller.getId());
                    conseillerDto.setNom(conseiller.getNom());
                    if(conseiller.getGerant()==null){
                        conseillerDto.setGerant_id(null);
                    }else{
                        conseillerDto.setGerant_id(conseiller.getGerant().getId());
                    }
                    if (conseiller.getClients().isEmpty()){
                            List<ClientVM> listClientVM = new ArrayList<>();
                        conseillerDto.setClients(listClientVM);
                    }else{
                        for (Client client : conseiller.getClients()){
                            ClientVM clientVM = new ClientVM(client.getId(), client.getNom(),client.getConseiller().getId());
                            conseillerDto.getClients().add(clientVM);
                        }
                    }
                    listConseillerDto.add(conseillerDto);
                }
            }
        }
        return Optional.of(listConseillerDto);
    }

    public Boolean verificationSiIdExiste(Long id){
        return conseillerRepository.existsById(id);
    }

    public Boolean creerConseiller(Optional<ConseillerDto> optionalConseillerDto){
        Optional<Conseiller> optionalConseiller = Optional.of(new Conseiller());

        optionalConseiller.get().setNom(optionalConseillerDto.get().getNom());
        if(optionalConseillerDto.get().getGerant_id()==null){
            optionalConseiller.get().setGerant(null);
        }else{
            Optional<Gerant> gerant = gerantRepository.findById(optionalConseillerDto.get().getGerant_id());
            optionalConseiller.get().setGerant(gerant.get());
        }
        optionalConseiller = Optional.of(conseillerRepository.save(optionalConseiller.get()));
        if(optionalConseiller.isPresent()) {
            for (ClientVM clientVM : optionalConseillerDto.get().getClients()) {
                if (clienService.verficationSiIdExiste(clientVM.getId())) {
                    assigneClientConseiller(clientVM.getId(), optionalConseiller.get().getId());
                }
            }
            optionalConseiller = Optional.of(conseillerRepository.save(optionalConseiller.get()));
            if(conseillerRepository.findById(optionalConseiller.get().getId()).isPresent()){
                return true;
            }else{
                return false;
            }
        }else {
            return false;
        }
    }

    public boolean creerConseillerSansClient(Optional<ConseillerDto> optionalConseillerDto) {
        Optional<Conseiller> optionalConseiller = Optional.of(new Conseiller());
        if(optionalConseillerDto.get().getGerant_id()==null){
            optionalConseiller.get().setGerant(null);
        }else{
            if(gerantRepository.existsById(optionalConseillerDto.get().getGerant_id())) {
                Optional<Gerant> gerant = gerantRepository.findById(optionalConseillerDto.get().getGerant_id());
                optionalConseiller.get().setGerant(gerant.get());
            }else{
                return false;
            }
        }
            List<Client> listClient = new ArrayList<>();
            optionalConseiller.get().setClients(listClient);
        optionalConseiller.get().setNom(optionalConseillerDto.get().getNom());
        if(optionalConseiller.isPresent()){
            optionalConseiller = Optional.of(conseillerRepository.save(optionalConseiller.get()));
            if(conseillerRepository.findById(optionalConseiller.get().getId()).isPresent()){
                return true;
            }else{
                return false;
            }
        }else {
            return false;
        }
    }

    public Boolean supprimerConseiller(Long id){
        Boolean resultat = conseillerRepository.existsById(id);
        if(resultat){
            Optional<Conseiller> conseiller = conseillerRepository.findById(id);

                conseillerRepository.deleteById(id);

            resultat = conseillerRepository.existsById(id);
            if (!resultat){
                return true;
            }else{
                return false;
            }
        }else {
            return false;
        }
    }

    public boolean verifieTailleListClientNull(Long id){
        Optional<Conseiller> conseiller = conseillerRepository.findById(id);
        if(conseiller.get().getClients().size()==0){
            return true;
        }else{
            return false;
        }
    }

    public Optional<ConseillerDto> findConseillerById(Long id){
        Optional<Conseiller> optionalConseiller=conseillerRepository.findById(id);
        if(optionalConseiller.isPresent()){
            ConseillerDto conseillerDto = new ConseillerDto();
            conseillerDto.setId(optionalConseiller.get().getId());
            conseillerDto.setNom(optionalConseiller.get().getNom());

            if(optionalConseiller.get().getGerant()==null){
                conseillerDto.setGerant_id(null);
            }else{
                conseillerDto.setGerant_id(optionalConseiller.get().getGerant().getId());
            }

            if (optionalConseiller.get().getClients().isEmpty()){
                List<ClientVM> listClientVM = new ArrayList<>();
                conseillerDto.setClients(listClientVM);
            }else{
                for (Client client : optionalConseiller.get().getClients()){
                    ClientVM clientVM = new ClientVM(client.getId(), client.getNom(),client.getConseiller().getId());
                    conseillerDto.getClients().add(clientVM);
                }
            }
            return Optional.of(conseillerDto);
        }else{
            return Optional.empty();
        }
    }

    public Boolean modifierConseillerById(Optional<ConseillerDto> conseillerDto, Long id){
        Optional<Conseiller> optionalConseiller = conseillerRepository.findById(id);
        if(optionalConseiller.isPresent()){
            optionalConseiller.get().setNom(conseillerDto.get().getNom());
            conseillerRepository.save(optionalConseiller.get());
            return true;
        }else{
            return false;
        }
    }

    public List<ClientVM> findClientsByConseillerId(Long id){
        Optional<List<ClientVM>>listclientVM = Optional.of(new ArrayList<ClientVM>());
        Optional<Conseiller> conseiller = conseillerRepository.findById(id);
        if(conseiller.isEmpty()){
            return new ArrayList<>();
        }else{
            for (Client client : conseiller.get().getClients()) {
                listclientVM.get().add(new ClientVM(client.getId(), client.getNom(), client.getConseiller().getId()));
            }
            return listclientVM.get();
        }
    }

    public Boolean assigneClientConseiller(Long id_client, Long id_conseiller){
        Optional<Conseiller> conseiller = conseillerRepository.findById(id_conseiller);
        Optional<Client> client = clientRepository.findById(id_client);
        if(conseiller.get().getClients().size()>=10){
            return false;
        }else {
            client.get().setConseiller(conseiller.get());
            conseiller.get().getClients().add(client.get());
            conseillerRepository.save(conseiller.get());
            clientRepository.save(client.get());
            return true;
        }
    }

    public Boolean desassigneClientConseiller(Long id_client, Long id_conseiller){
        Optional<Conseiller> conseiller = conseillerRepository.findById(id_conseiller);
        Optional<Client> client = clientRepository.findById(id_client);
        for (Client sonclient : conseiller.get().getClients()) {
            if(sonclient.getId()==id_client){
                client.get().setConseiller(null);
                conseiller.get().getClients().remove(client.get());
                conseillerRepository.save(conseiller.get());
                clientRepository.save(client.get());
                return true;
            }
        }
        return false;
    }

    public Optional<ClientVM> findClientIdByConseillerID (Long id_client, Long id_conseiller){
        Optional<Conseiller> conseiller = conseillerRepository.findById(id_conseiller);
        ClientVM clientVM =new ClientVM();
        for (Client sonclient : conseiller.get().getClients()) {
            if(sonclient.getId()==id_client){
                clientVM.setId(sonclient.getId());
                clientVM.setNom(sonclient.getNom());
                clientVM.setConseiller_id(sonclient.getConseiller().getId());
                return Optional.of(clientVM);
            }
        }
        return Optional.empty();
    }

    public Optional<List<ConseillerDto>> findConseillerDisponible(Integer gerantId) {

        Optional<List<ConseillerDto>> listConseiller = findAllConseiller();
        List<ConseillerDto> listconseillerDisponible  = new ArrayList<>();

        for (ConseillerDto conseiller: listConseiller.get()) {
            if(conseiller.getGerant_id()==gerantId){
                if(conseiller.getClients().size()<11){
                    listconseillerDisponible.add(conseiller);
                }
            }
        }

        return Optional.of(listconseillerDisponible);
    }
}
